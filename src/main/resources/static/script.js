//Size of the board and column labels
const size = 10;
const letters = "ABCDEFGHIJ";

/**
 * Tracks whether the game has ended (WIN or LOSS).
 * Used to block further attacks after the game is over.
 */
let gameOver= false;

// User Feedback button
const USER_FEEDBACK_LINK = 'https://docs.google.com/forms/d/e/1FAIpQLSdr2xzx1jbwUW6hDL321aXq4rXhb8n56_qPCpv8hvU4RCcTCA/viewform';
function handleFeedbackClick() {
    window.open(USER_FEEDBACK_LINK, '_blank');
}

/**
 * Builds a 10x10 board inside the given table element.
 * @param {string} tableId - The ID of the table element to populate.
 * @param {boolean} clickable - If true, each cell fires submitAttack on click (used for the computer's board).
 */

function loadBoard(tableId, clickable) {
    const table = document.getElementById(tableId);
    table.innerHTML = '';
    // Top header row (A-J)
    const headerRow = table.insertRow();
    headerRow.insertCell(); // empty corner
    for (let j = 0; j < size; j++) {
        const th = document.createElement("th");
        th.textContent = letters[j];
        th.className = "label";
        headerRow.appendChild(th);
    }
    for (let i = 0; i < size; i++) {
        const row = table.insertRow();
        const labelCell = document.createElement("th");

        labelCell.textContent = String(i + 1);
        labelCell.className = "label";
        row.appendChild(labelCell);

        for (let j = 0; j < size; j++) {
            const cell = row.insertCell();
            cell.className = "water";
            cell.textContent = "~";
            if (clickable) {
                cell.onclick = function () {
                    submitAttack(i, j).catch(console.error);
                };
            }
        }
    }
}


/**
 * Sends the player's chosen cell to the backend and updates the UI.
 * Handles both the player's attack result and the computer's counter-move.
 * Ends the game if the backend returns a non-IN_PROGRESS status.
 * @param {number} row - Zero-based row index of the attacked cell.
 * @param {number} col - Zero-based column index of the attacked cell.
 */

/**
 * @typedef {Object} AttackResponse
 * @property {boolean} isError
 * @property {string[][]} grid
 * @property {string[][]} homeGrid
 * @property {number} computerRow
 * @property {number} computerCol
 * @property {number} guessesLeft
 * @property {string} message
 * @property {string} computerMessage
 * @property {string} gameStatus
 */

async function submitAttack(row, col) {
    if (gameOver) return;

    const response = await fetch("api/battleship/attack", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ row: row, column: col })
    });

    /** @type {AttackResponse} */
    const data = await response.json();

    if (data.isError) {
        document.getElementById("message").innerText = "Cell already attacked.";
        return;
    }

    updateComputerBoardCell(row, col, data.grid[row][col]);

    /* Repaint the whole human-board and highlight the computer's move */
    renderHumanBoard(data.homeGrid);
    if (data.computerRow >= 0) {
        highlightLastComputerMove(data.computerRow, data.computerCol);
    }

    document.getElementById("guesses-left").innerText = `Guesses left: ${data.guessesLeft}`;

    /* Show both player and computer messages, handle WIN/LOSS */
    document.getElementById("message").innerText = data.message;
    document.getElementById("computer-message").innerText = data.computerMessage || "";

    if (data.gameStatus !== "IN_PROGRESS") {
        gameOver = true;
        if (data.gameStatus === "WIN") {
            document.getElementById("message").innerText = "You Won!";
        } else {
            document.getElementById("message").innerText = "Computer Wins!";
        }
    }
}

/**
 * Updates a single cell on the computer's board after an attack.
 * @param {number} row - Zero-based row index.
 * @param {number} col - Zero-based column index.
 * @param {string} newState - Either "hit" or "miss" from the backend response.
 */
function updateComputerBoardCell(row, col, newState) {
    const table = document.getElementById("computer-board");
    const cell = table.rows[row + 1].cells[col + 1];
    if (newState === "hit") {
        cell.className = "hit";
        cell.textContent = "X";
    } else {
        cell.className = "miss";
        cell.textContent = "O";
    }
}

/**
 * Repaints the entire human board based on the latest grid state from the backend.
 * @param {string[][]} homeGrid - 2D array of cell states: "hit", "miss", "ship", or "water".
 */

function renderHumanBoard(homeGrid) {
    const table = document.getElementById("human-board");
    for (let r = 0; r < homeGrid.length; r++) {
        for (let c = 0; c < homeGrid[r].length; c++) {
            const cell = table.rows[r + 1].cells[c + 1];
            const state = homeGrid[r][c].toLowerCase();
            cell.className = state;
            if (state === "hit")       cell.textContent = "X";
            else if (state === "miss") cell.textContent = "O";
            else if (state === "ship") cell.textContent = "#";
            else                       cell.textContent = "~";
        }
    }
}

/**
 * Highlights the cell the computer attacked last turn with an orange outline.
 * Removes the highlight from the previously marked cell first.
 * @param {number} row - Zero-based row index of the computer's move.
 * @param {number} col - Zero-based column index of the computer's move.
 */
function highlightLastComputerMove(row, col) {
    const prev = document.querySelector(".last-computer-move");
    if (prev) prev.classList.remove("last-computer-move");
    const table = document.getElementById("human-board");
    table.rows[row + 1].cells[col + 1].classList.add("last-computer-move");
}

async function loadVersion() {
    const response = await fetch("api/version");
    const version = await response.text(); //https://stackoverflow.com/questions/41946457/getting-text-from-fetch-response-object
    document.getElementById("version").innerText = "Version: " + version;
}

// On page load: start a new game, build both boards, and show the player's ships
window.onload = async function () {
    const response = await fetch("api/battleship/start-game", { method: "POST" });
    const guessesLeft = await response.json();
    document.getElementById("guesses-left").innerText = `Guesses left: ${guessesLeft}`;

    /* Build both boards, computer-board is clickable, human-board is not */
    loadBoard("computer-board", true);
    loadBoard("human-board", false);

    /* Fetch humanStatus so the player's ships show up before the first attack */
    const statusResponse = await fetch("api/battleship/humanStatus");
    const humanDTO = await statusResponse.json();
    renderHumanBoard(humanDTO.homeGrid);
    await loadVersion();
};
"use client";
import { nanoid } from "nanoid";
import { useEffect, useRef, useState } from "react";
import Confetti from "react-confetti";
import { Comfortaa, Poppins } from "next/font/google";
import Popout from "../(components)/popout";
import NavBar from "../../(components)/navBar";
import { useRouter } from "next/navigation";
import Background from "../(components)/Background";
import FetchingClass from "../(components)/Fetching";
import Swal from "sweetalert2";
import BottomNav from "../(components)/BottomNav";

const Comfor = Comfortaa({
  weight: ["400", "500", "600", "700"],
  subsets: ["latin", "latin-ext"],
});

const Pop = Poppins({
  weight: ["400", "500", "600", "700", "800", "900"],
  subsets: ["latin", "latin-ext"],
});

export default function page() {
  const obj = new FetchingClass();
  const router = useRouter();

  const token = window.localStorage.getItem("token");

  if (!token) {
    router.push("/login");
  }

  const [TTT, setTTT] = useState({
    board: [[]],
    win: false,
    end: { status: 200, end: false },
    difficulty: "easy",
  });
  const [gameType, setGameType] = useState("PvPC");
  const [round, setRound] = useState(0);
  const preventInitialRender = useRef(false);

  useEffect(() => {
    if (preventInitialRender.current === false) {
      preventInitialRender.current = true;
    } else {
      handleRestart();
    }
  }, [gameType]);

  useEffect(() => {
    (async () => {
      try {
        const email = window.localStorage.getItem("email");
        const res = await fetch(
          `http://localhost:8080/treblecross/${email}/board`
        );

        const board = await res.json();
        setTTT((prev) => ({ ...prev, board: board }));

        if (board.flat(1).includes("X")) {
          const element = document.getElementById("selectDif");
          element?.setAttribute("disabled", "true");
        }

        const response = await fetch(
          `http://localhost:8080/treblecross/${email}/getdifficulty`
        );
        const difficulty = await response.json();

        setTTT((prev) => ({ ...prev, difficulty: difficulty.difficulty }));
      } catch (err) {
        console.log(err);
      }
    })();
  }, []);

  async function handleChange(e: any) {
    try {
      const email = window.localStorage.getItem("email");
      if (email) {
        const { value } = e.target;
        const res = await obj.FetchDifficulty(value, "/treblecross", email);

        if (res.ok) {
          const dif = await res.json();
          setTTT((prev) => ({ ...prev, difficulty: dif.difficulty }));
        }
      }
    } catch (err) {
      console.log(err);
    }
  }

  async function handleBackMove() {
    try {
      const email = window.localStorage.getItem("email");
      await fetch(`http://localhost:8080/treblecross/${email}/backMove`);
      const res = await fetch(
        `http://localhost:8080/treblecross/${email}/board`
      );
      const board = await res.json();

      setTTT((prev) => ({
        ...prev,
        board: board,
      }));
    } catch (err) {
      console.log(err);
    }
  }

  async function handleClick(whichRow: number, whichCol: number) {
    try {
      const email = window.localStorage.getItem("email");
      if (TTT.end.end !== true) {
        const element = document.getElementById("selectDif");
        element?.setAttribute("disabled", "true");

        if (email) {
          const isWin = await obj.FetchPlayerMove(
            whichRow,
            whichCol,
            email,
            TTT.difficulty,
            "/treblecross"
          );

          if (isWin === "Invalid Move!") {
            Swal.fire({ title: "Invalid Move!", icon: "error" });
          }

          const res = await fetch(
            `http://localhost:8080/treblecross/${email}/board`
          );
          const board = await res.json();

          setTTT((prev) => ({
            ...prev,
            board: board,
          }));

          if (isWin === 1 || isWin === 0 || isWin === -1) {
            if (isWin === 1) {
              setTTT((prev) => ({
                ...prev,
                win: true,
                end: { status: isWin, end: true },
                board: board,
              }));
            } else {
              setTTT((prev) => ({
                ...prev,
                win: false,
                end: { status: isWin, end: true },
                board: board,
              }));
            }
          }
        }
      }
    } catch (err) {
      console.log(err);
    }
  }

  async function handlePVPMove(whichRow: number) {
    try {
      let turn;
      if (round % 2 === 0) {
        turn = "PLAYER";
      } else {
        turn = "AI";
      }

      const email = window.localStorage.getItem("email");
      if (TTT.end.end !== true) {
        const element = document.getElementById("selectDif");
        element?.setAttribute("disabled", "true");

        if (email) {
          const isWin = await obj.FetchTreblecrossPVPMove(
            whichRow,
            email,
            TTT.difficulty,
            "/treblecross",
            turn
          );

          const res = await fetch(
            `http://localhost:8080/treblecross/${email}/board`
          );
          const board = await res.json();

          setTTT((prev) => ({
            ...prev,
            board: board,
          }));

          if (isWin === 1 || isWin === 0 || isWin === -1) {
            if (isWin === 1) {
              setTimeout(() => {}, 200);
              setTTT((prev) => ({
                ...prev,
                win: true,
                end: { status: isWin, end: true },
                board: board,
              }));
            } else {
              setTimeout(() => {}, 200);
              setTTT((prev) => ({
                ...prev,
                win: false,
                end: { status: isWin, end: true },
                board: board,
              }));
            }
          }

          setRound((prev) => prev + 1);
        }
      }
    } catch (err) {
      console.log(err);
    }
  }

  async function handleEvEMove() {
    try {
      let turn;
      let isMax;
      if (round % 2 === 0) {
        turn = "PLAYER";
        isMax = true;
      } else {
        turn = "AI";
        isMax = false;
      }

      const email = window.localStorage.getItem("email");
      if (TTT.end.end !== true) {
        const element = document.getElementById("selectDif");
        element?.setAttribute("disabled", "true");

        if (email) {
          let isWin = await obj.FetchEvEMove(
            email,
            "/treblecross",
            turn,
            isMax
          );

          isWin = Number(isWin);

          if (turn == "AI" && isWin == 1) {
            isWin = -1;
          }

          const res = await fetch(
            `http://localhost:8080/treblecross/${email}/board`
          );
          const board = await res.json();

          setTTT((prev) => ({
            ...prev,
            board: board,
          }));

          if (isWin === 1 || isWin === -1) {
            setTimeout(() => {}, 200);
            setTTT((prev) => ({
              ...prev,
              win: isWin === 1,
              end: { status: isWin, end: true },
              board: board,
            }));
          }
          setRound((prev) => prev + 1);
        }
      }
    } catch (err) {
      console.log(err);
    }
  }

  async function handleRestart() {
    try {
      const email = window.localStorage.getItem("email");
      const element = document.getElementById("selectDif");
      element?.removeAttribute("disabled");
      await fetch(`http://localhost:8080/treblecross/${email}/restart`);
      const res = await fetch(
        `http://localhost:8080/treblecross/${email}/board`
      );
      const board = await res.json();
      setTTT((prev) => ({
        ...prev,
        board: board,
        win: false,
        end: { status: 200, end: false },
      }));
      setRound(0);
    } catch (err) {
      console.log(err);
    }
  }

  async function handleSave() {
    const email = window.localStorage.getItem("email");
    Swal.fire({
      title: "Please enter game name",
      input: "text",
      showCancelButton: true,
      confirmButtonText: "Save",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          if (email) {
            await obj.FetchSaveGame(email, "/treblecross", result.value);
            handleRestart();
          }
        } catch (err) {
          console.log(err);
        }
      }
    });
  }

  return (
    <div
      className={`h-screen w-full flex justify-center items-center overflow-hidden ${Comfor.className}`}
    >
      <NavBar />
      <BottomNav gameType={gameType} setGameType={setGameType} />
      {TTT.end.end && <Popout status={TTT.end.status} gameType={gameType} />}
      <Background />
      {gameType === "PvPC" && TTT.win && (
        <Confetti className="overflow-hidden" />
      )}
      <div
        className={`fixed left-0 right-0 z-[3] h-[32rem] backdrop-blur-sm bg-[rgba(255,255,255,0.4)] border-none px-10 py-8 flex flex-col ${
          gameType === "PvP" ? "justify-evenly" : "justify-around"
        } items-center`}
      >
        {gameType !== "PvP" ? (
          <select
            className="px-4 py-2 bg-transparent border-2 border-black cursor-pointer"
            name="difficulty"
            value={TTT.difficulty}
            onChange={handleChange}
            id="selectDif"
          >
            <option value="easy">Easy</option>
            <option value="medium">Medium</option>
            <option value="hard">Hard</option>
          </select>
        ) : (
          ""
        )}
        <div
          className={`z-[3] rounded-2xl font-extrabold grid grid-rows-1 grid-flow-col justify-center`}
        >
          {TTT.board[0].map((insideBoard, index) => {
            return (
              <div
                onClick={() => {
                  if (gameType === "PvPC") {
                    handleClick(index, 0);
                  } else if (gameType === "PvP") {
                    handlePVPMove(index);
                  } else if (gameType === "PCvPC") {
                    ("");
                  }
                }}
                key={nanoid()}
                className={`${
                  index !== 10 ? "border-r-0" : ""
                } text-[4rem] flex justify-center items-center cursor-pointer text-5xl w-[6rem] h-[6rem] border-4 border-black`}
              >
                <h1
                  className={`pointer-events-none translate-y-2 text-red-400`}
                >
                  {insideBoard === "-" ? "" : "X"}
                </h1>
              </div>
            );
          })}
        </div>
        <div className="flex gap-4">
          <button
            onClick={() => {
              Swal.fire({
                title: "Confirm Save?",
                text: "Your current progress will be reset as well.",
                icon: "warning",
              }).then((result) => {
                if (result.isConfirmed) {
                  handleSave();
                }
              });
            }}
            className={`${Pop.className} ${
              TTT.end.end ? "hidden" : gameType === "PvP" ? "hidden" : ""
            } z-[3] font-bold p-[0.6rem_2rem] text-lg rounded-2xl border-2 border-black`}
          >
            Save
          </button>
          <button
            onClick={() => {
              if (gameType === "PvPC") {
                Swal.fire({
                  title: "Confirm Undo?",
                  text: "Only Noobs Do This!",
                  icon: "warning",
                }).then((result) => {
                  if (result.isConfirmed) {
                    handleBackMove();
                  }
                });
              } else if (gameType === "PCvPC") {
                handleEvEMove();
              }
            }}
            className={`${Pop.className} ${
              TTT.end.end ? "hidden" : gameType === "PvP" ? "hidden" : ""
            } z-[3] font-bold p-[0.6rem_2rem] text-lg rounded-2xl border-2 border-black`}
          >
            {gameType === "PCvPC" ? "Next" : "Back"}
          </button>

          <button
            onClick={handleRestart}
            className={`${Pop.className} ${
              TTT.end.end ? "" : gameType === "PvP" ? "" : "hidden"
            } z-[3] font-bold p-[0.6rem_2rem] text-lg rounded-2xl border-2 border-black`}
          >
            Restart
          </button>
        </div>
      </div>
    </div>
  );
}

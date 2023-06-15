"use client";

import FetchingClass from "@/app/tictactoe/(components)/Fetching";
import { nanoid } from "nanoid";
import { Comfortaa } from "next/font/google";
import { useEffect, useState } from "react";
import Swal from "sweetalert2";

const Comfor = Comfortaa({
  weight: ["400", "500", "600", "700"],
  subsets: ["latin", "latin-ext"],
});

export default function MisereTicTacToe({
  setTicTacToe,
  difficulty,
}: {
  setTicTacToe: Function;
  difficulty: string;
}) {
  const obj = new FetchingClass();
  const [TTT, setTTT] = useState({
    board: [[], [], []],
    win: false,
    end: { status: 200, end: false },
    difficulty: difficulty,
  });

  useEffect(() => {
    (async () => {
      try {
        const email = window.localStorage.getItem("email");
        const res = await fetch(
          `http://localhost:8080/mtictactoe/${email}/board`
        );
        const board = await res.json();
        setTTT((prev) => ({ ...prev, board: board }));
      } catch (err) {
        console.log(err);
      }
    })();
  }, []);

  async function handleRestart() {
    try {
      const email = window.localStorage.getItem("email");

      await fetch(`http://localhost:8080/mtictactoe/${email}/restart`);
      const res = await fetch(
        `http://localhost:8080/mtictactoe/${email}/board`
      );
      const board = await res.json();
      setTTT((prev) => ({
        ...prev,
        board: board,
        win: false,
        end: { status: 200, end: false },
      }));
    } catch (err) {
      console.log(err);
    }
  }

  async function handleClick(whichRow: number, whichCol: number) {
    try {
      if (TTT.end.end !== true) {
        const email = window.localStorage.getItem("email");
        const element = document.getElementById("selectDif");
        element?.setAttribute("disabled", "true");

        if (email) {
          const isWin = await obj.FetchPlayerMoveStory(
            whichRow,
            whichCol,
            email,
            TTT.difficulty,
            "/mtictactoe"
          );

          const res = await fetch(
            `http://localhost:8080/mtictactoe/${email}/board`
          );
          const board = await res.json();
          if (isWin === "Invalid Move!") {
            Swal.fire({ title: "Invalid Move!", icon: "error" });
          }

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
              setTicTacToe(1);
              const element = document.getElementById(
                "MisereTicTacToe"
              ) as HTMLDivElement;
              element.style.display = "none";
              handleRestart();
              Swal.fire({
                title: "You Win!",
              });
            } else {
              setTimeout(() => {}, 200);
              setTTT((prev) => ({
                ...prev,
                win: false,
                end: { status: isWin, end: true },
                board: board,
              }));
              if (isWin === -1) {
                setTicTacToe(-1);
              } else if (isWin === 0) {
                setTicTacToe(0);
              }
              const element = document.getElementById(
                "MisereTicTacToe"
              ) as HTMLDivElement;
              element.style.display = "none";
              handleRestart();
              Swal.fire({
                title: `${isWin === -1 ? "You Lose!" : "Tie!"}`,
              });
            }
          }
        }
      }
    } catch (err) {
      console.log(err);
    }
  }

  return (
    <div
      id="MisereTicTacToe"
      className="fixed w-full h-screen overflow-hidden grid place-items-center bg-transparent"
    >
      <div className="w-[800px] h-[540px] translate-y-[-30px] flex flex-col justify-center items-center gap-10 backdrop-blur-lg p-10 rounded-md border-1 border-black">
        <h1 className="text-2xl font-bold">Misere Tic Tac Toe</h1>
        <div
          className={`${Comfor.className} z-[3] rounded-2xl font-extrabold grid grid-cols-[6rem_6rem_6rem] justify-center`}
        >
          {TTT.board.map((board1, index1) => {
            return board1.map((insideBoard, index) => {
              return (
                <div
                  onClick={() => {
                    handleClick(index1, index);
                  }}
                  key={nanoid()}
                  className={`${index === 0 ? "border-l-0" : ""} ${
                    index === 2 ? "border-r-0" : ""
                  } ${
                    index1 === 0
                      ? "border-t-0"
                      : index1 === 2
                      ? "border-b-0"
                      : ""
                  } text-[4rem] flex justify-center items-center cursor-pointer text-5xl w-[6rem] h-[6rem] border-4 border-black`}
                >
                  <h1
                    className={`${
                      insideBoard === "X" ? "text-red-400" : "text-emerald-400"
                    } pointer-events-none translate-y-2`}
                  >
                    {insideBoard === "-" ? "" : insideBoard === "X" ? "X" : "O"}
                  </h1>
                </div>
              );
            });
          })}
        </div>
      </div>
    </div>
  );
}

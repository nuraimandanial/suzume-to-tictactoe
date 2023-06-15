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

export default function Treblecross({
  setTicTacToe,
  difficulty,
}: {
  setTicTacToe: Function;
  difficulty: string;
}) {
  const obj = new FetchingClass();
  const [TTT, setTTT] = useState({
    board: [[]],
    win: false,
    end: { status: 200, end: false },
    difficulty: difficulty,
  });

  useEffect(() => {
    (async () => {
      try {
        const email = window.localStorage.getItem("email");
        const res = await fetch(
          `http://localhost:8080/treblecross/${email}/board`
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
          const isWin = await obj.FetchPlayerMoveStory(
            whichRow,
            whichCol,
            email,
            TTT.difficulty,
            "/treblecross"
          );

          const res = await fetch(
            `http://localhost:8080/treblecross/${email}/board`
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
              setTTT((prev) => ({
                ...prev,
                win: true,
                end: { status: isWin, end: true },
                board: board,
              }));
              setTicTacToe(1);
              const element = document.getElementById(
                "Treblecross"
              ) as HTMLDivElement;
              element.style.display = "none";
              handleRestart();
              Swal.fire({
                title: "You Win!",
              });
            } else {
              setTTT((prev) => ({
                ...prev,
                win: false,
                end: { status: isWin, end: true },
                board: board,
              }));
              setTicTacToe(-1);
              const element = document.getElementById(
                "Treblecross"
              ) as HTMLDivElement;
              element.style.display = "none";
              handleRestart();
              Swal.fire({
                title: "You Lose!",
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
      id="Treblecross"
      className="fixed w-full h-screen overflow-hidden grid place-items-center bg-transparent"
    >
      <div className="w-[800px] h-[540px] translate-y-[-30px] flex flex-col justify-center items-center gap-10 backdrop-blur-lg p-10 rounded-md border-1 border-black">
        <h1 className="text-2xl font-bold">Treblecross</h1>
        <div
          className={`${Comfor.className} z-[3] rounded-2xl font-extrabold grid grid-rows-1 grid-flow-col justify-center`}
        >
          {TTT.board[0].map((insideBoard, index) => {
            return (
              <div
                onClick={() => {
                  handleClick(index, 0);
                }}
                key={nanoid()}
                className={`${
                  index !== 10 ? "border-r-0" : ""
                } text-[2rem] flex justify-center items-center cursor-pointer text-5xl w-[3rem] h-[3rem] border-2 border-black`}
              >
                <h1
                  className={`pointer-events-none translate-y-1 text-red-400`}
                >
                  {insideBoard === "-" ? "" : "X"}
                </h1>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}

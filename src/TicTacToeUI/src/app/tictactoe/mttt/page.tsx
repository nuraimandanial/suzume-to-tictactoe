"use client";
import { nanoid } from "nanoid";
import { useEffect, useState } from "react";
import Confetti from "react-confetti";
import { Comfortaa, Poppins } from "next/font/google";
import Popout from "./(components)/popout";
import NavBar from "../../(components)/navBar";
import { useRouter } from "next/navigation";
import Background from "./(components)/Background";
import FetchingClass from "../(components)/Fetching";

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
  const [TTT, setTTT] = useState({
    board: [[], [], []],
    win: false,
    end: { status: 200, end: false },
    difficulty: "easy",
  });

  useEffect(() => {
    const token = window.localStorage.getItem("token");

    if (!token) {
      router.push("/login");
    }

    (async () => {
      try {
        const email = window.localStorage.getItem("email");
        const res = await fetch(
          `http://localhost:8080/mtictactoe/${email}/board`
        );
        const board = await res.json();
        setTTT((prev) => ({ ...prev, board: board }));

        if (board.flat(1).includes("X")) {
          const element = document.getElementById("selectDif");
          element?.setAttribute("disabled", "true");
        }

        const response = await fetch(
          `http://localhost:8080/mtictactoe/${email}/getdifficulty`
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
        const res = await obj.FetchDifficulty(value, "/mtictactoe", email);
        if (res.ok) {
          const dif = await res.json();
          setTTT((prev) => ({ ...prev, difficulty: dif.difficulty }));
        }
      }
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
          const isWin = await obj.FetchPlayerMove(
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

          setTTT((prev) => ({
            ...prev,
            board: board,
          }));

          if (isWin !== 200) {
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

  async function handleSave() {
    try {
      const email = window.localStorage.getItem("email");
      if (email) {
        await obj.FetchSaveGame(email, "/mtictactoe");
      }
    } catch (err) {
      console.log(err);
    }
  }

  return (
    <div
      className={`h-screen w-full flex justify-center items-center overflow-hidden ${Comfor.className}`}
    >
      <NavBar />
      {TTT.end.end && <Popout status={TTT.end.status} />}
      <Background />
      {TTT.win && <Confetti className="overflow-hidden" />}
      <div className="fixed left-0 right-0 z-[3] h-[32rem] backdrop-blur-sm bg-[rgba(255,255,255,0.4)] border-none px-10 py-8 flex flex-col justify-around items-center">
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
        <div className="z-[3] rounded-2xl font-extrabold grid grid-cols-[6rem_6rem_6rem] justify-center">
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
        <div className="flex gap-4">
          <button
            onClick={handleSave}
            className={`${Pop.className} z-[3] font-bold p-[0.6rem_2rem] text-lg rounded-2xl border-2 border-black`}
          >
            Save
          </button>
          <button
            onClick={handleRestart}
            className={`${Pop.className} z-[3] font-bold p-[0.6rem_2rem] text-lg rounded-2xl border-2 border-black`}
          >
            Restart
          </button>
        </div>
      </div>
    </div>
  );
}

"use client";
import { nanoid } from "nanoid";
import { useEffect, useState } from "react";
import { Comfortaa } from "next/font/google";
import { useRouter } from "next/navigation";
import HomeMethod from "./HomeMethod";
import Swal from "sweetalert2";

const Comfor = Comfortaa({
  weight: ["400", "500", "600", "700"],
  subsets: ["latin", "latin-ext"],
});

export default function FFTTT() {
  const obj = new HomeMethod();
  const router = useRouter();
  const [loadGameCriteria, setLoadGameCriteria] = useState({
    board: [[]],
    gameId: [],
    difficulty: [],
    gameType: [],
    name: [],
    email: "",
  });

  useEffect(() => {
    const email = window.localStorage.getItem("email");

    if (email) {
      setLoadGameCriteria((prev) => ({
        ...prev,
        email: email,
      }));
      (async () => {
        const board = await obj.ExtractLoadedBoard(email, "ffttt");
        if (board) {
          setLoadGameCriteria((prev) => ({
            ...prev,
            board: board.board,
            gameId: board.id,
            difficulty: board.difficulty,
            gameType: board.game,
            name: board.name,
          }));
        }
      })();
    }
  }, []);

  function changePage(number: number) {
    const element = document.getElementById(`carou2`);

    if (element) {
      element.scrollLeft += 400 * number;
    }
  }

  async function handleLoadGame(id: number, game: string) {
    const email = window.localStorage.getItem("email");
    const res = await fetch(
      `http://localhost:8080/fftictactoe/${email}/loadgame`,
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ id: id.toString() }),
      }
    );

    handleDelete(id);
    if (res.ok) {
      router.push(`/tictactoe/${game}`);
    }
  }

  async function handleDelete(id: number) {
    const message = await obj.DeleteSavedGame(id);
    const board = await obj.ExtractLoadedBoard(loadGameCriteria.email, "ffttt");
    if (board) {
      setLoadGameCriteria((prev) => ({
        ...prev,
        board: board.board,
        gameId: board.id,
        difficulty: board.difficulty,
        gameType: board.game,
      }));
    }
  }

  return (
    <div className="relative">
      <button
        onClick={() => changePage(-1)}
        className={`${
          loadGameCriteria.gameId.length <= 1 ? "hidden" : ""
        } absolute top-1/2 bottom-1/2 left-10 -translate-y-1/2 z-[99] h-16 w-16 rounded-full bg-black text-white text-5xl grid place-items-center`}
      >
        <p>{"<"}</p>
      </button>

      <button
        onClick={() => changePage(1)}
        className={`${
          loadGameCriteria.gameId.length <= 1 ? "hidden" : ""
        } absolute top-1/2 bottom-1/2 right-10 -translate-y-1/2 z-[99] h-16 w-16 rounded-full bg-black text-white text-5xl grid place-items-center`}
      >
        <p>{">"}</p>
      </button>
      <div
        id={"carou2"}
        className={`${
          loadGameCriteria.gameId.length <= 1
            ? "overflow-hidden"
            : "overflow-scroll"
        } scroll-smooth hideScroll snap-x snap-mandatory py-10 w-[40rem] border-2 border-white text-white rounded-3xl backdrop-blur-xl`}
      >
        <div
          className={`${Comfor.className} z-[3] font-extrabold grid grid-rows-1 grid-flow-col gap-4`}
        >
          {loadGameCriteria.board.length > 0 ? (
            loadGameCriteria.board.map((insideBoard, index) => {
              return (
                <section
                  key={nanoid()}
                  className="snap-center w-[40rem] z-[3] flex flex-col font-extrabold justify-center place-items-center"
                >
                  <h1 className="text-3xl font-bold mb-8 uppercase">
                    {`${loadGameCriteria.name[index]} ( ${loadGameCriteria.difficulty[index]} )`}
                  </h1>

                  <div className="grid grid-cols-[4rem_4rem_4rem_4rem_4rem]">
                    {insideBoard.flat(1).map((lastBoard, index2) => {
                      return (
                        <div
                          key={nanoid()}
                          className={`${
                            index2 === 0 ||
                            index2 === 5 ||
                            index2 === 10 ||
                            index2 === 15 ||
                            index2 === 20
                              ? "border-l-[3.5px]"
                              : ""
                          } ${
                            index2 === 4 ||
                            index2 === 9 ||
                            index2 === 14 ||
                            index2 === 19 ||
                            index2 === 24
                              ? "border-r-[3.5px]"
                              : ""
                          } ${
                            index2 === 0 ||
                            index2 === 1 ||
                            index2 === 2 ||
                            index2 === 3 ||
                            index2 === 4
                              ? "border-t-[3.5px]"
                              : ""
                          } ${
                            index2 === 20 ||
                            index2 === 21 ||
                            index2 === 22 ||
                            index2 === 23 ||
                            index2 === 24
                              ? "border-b-[3.5px]"
                              : ""
                          } text-[2rem] flex justify-center items-center text-5xl w-[4rem] h-[4rem] border-2 border-white`}
                        >
                          <h1
                            className={`${
                              lastBoard === "X"
                                ? "text-red-400"
                                : "text-emerald-400"
                            } pointer-events-none translate-y-2`}
                          >
                            {lastBoard === "-"
                              ? ""
                              : lastBoard === "X"
                              ? "X"
                              : "O"}
                          </h1>
                        </div>
                      );
                    })}
                  </div>
                  <div className="flex gap-10">
                    <button
                      onClick={() =>
                        Swal.fire({
                          title: "Load this game?",
                          text: "After resuming, this record will be permanently deleted",
                          icon: "warning",
                          showCancelButton: true,
                          confirmButtonColor: "#3085d6",
                          cancelButtonColor: "#d33",
                          confirmButtonText: "Yes",
                        }).then((result) => {
                          if (result.isConfirmed) {
                            handleLoadGame(
                              loadGameCriteria.gameId[index],
                              loadGameCriteria.gameType[index]
                            );
                          }
                        })
                      }
                      className="grid col-start-1 px-3 py-2 w-40 bg-red-300 text-black rounded-xl mt-10"
                    >
                      Load Game
                    </button>
                    <button
                      onClick={() =>
                        Swal.fire({
                          title: "Are you sure?",
                          text: "You won't be able to revert this!",
                          icon: "warning",
                          showCancelButton: true,
                          confirmButtonColor: "#3085d6",
                          cancelButtonColor: "#d33",
                          confirmButtonText: "Yes, delete it!",
                        }).then((result) => {
                          if (result.isConfirmed) {
                            handleDelete(loadGameCriteria.gameId[index]);
                            Swal.fire(
                              "Deleted!",
                              "Your game has been deleted.",
                              "success"
                            );
                          }
                        })
                      }
                      className="grid col-start-5 px-3 py-2 w-40 bg-red-300 text-black rounded-xl mt-10"
                    >
                      Delete Game
                    </button>
                  </div>
                </section>
              );
            })
          ) : (
            <section
              key={nanoid()}
              className="snap-center w-[40rem] h-[18rem] z-[3] font-extrabold grid place-items-center text-3xl"
            >
              <h1>No Saved Game!</h1>
            </section>
          )}
        </div>
      </div>
    </div>
  );
}

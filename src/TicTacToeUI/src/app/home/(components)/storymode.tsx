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

export default function StoryMode() {
  const obj = new HomeMethod();
  const router = useRouter();
  const [loadGameCriteria, setLoadGameCriteria] = useState({
    gameId: [],
    difficulty: [],
    map: [],
    name: [],
    score: [],
    win: [],
    lose: [],
    path: [],
  });
  const [initialUpdate, setInitialUpdate] = useState(0);

  useEffect(() => {
    const email = window.localStorage.getItem("email");

    if (email) {
      (async () => {
        const res = await fetch(
          `http://localhost:8080/database/${email}/loadgame/storymode`
        );

        if (res) {
          const loadedboard = await res.json();
          const id = JSON.parse(loadedboard.id);
          const difficulty = JSON.parse(
            loadedboard.difficulty
              .replace("[", '["')
              .replace("]", '"]')
              .replaceAll(", ", '", "')
          );

          const maze = JSON.parse(loadedboard.map);
          const name = JSON.parse(
            loadedboard.name
              .replace("[", '["')
              .replace("]", '"]')
              .replaceAll(", ", '", "')
          );
          const win = JSON.parse(loadedboard.win);

          const lose = JSON.parse(loadedboard.lose);
          const score = JSON.parse(loadedboard.score);
          const path = JSON.parse(loadedboard.path);
          setLoadGameCriteria((prev) => ({
            gameId: id,
            difficulty: difficulty,
            map: maze,
            win,
            lose,
            name,
            score,
            path,
          }));
        }
      })();
      if (initialUpdate !== 2) {
        setInitialUpdate((prev) => prev + 1);
      }
    }
  }, [initialUpdate]);

  function changePage(number: number) {
    const element = document.getElementById(`carou4`);

    if (element) {
      element.scrollLeft += 700 * number;
    }
  }

  async function handleLoadGame(id: number) {
    const email = window.localStorage.getItem("email");
    const res = await fetch(
      `http://localhost:8080/connectingthedots/${email}/loadGame/${id}`
    );

    handleDelete(id);
    router.push(`/maze`);
  }

  async function handleDelete(id: number) {
    const email = window.localStorage.getItem("email");
    await fetch(
      `http://localhost:8080/connectingthedots/${email}/deleteGame/${id}`
    );

    const res = await fetch(
      `http://localhost:8080/database/${email}/loadgame/storymode`
    );

    if (res) {
      const loadedboard = await res.json();
      const id = JSON.parse(loadedboard.id);
      const difficulty = JSON.parse(
        loadedboard.difficulty
          .replace("[", '["')
          .replace("]", '"]')
          .replaceAll(", ", '", "')
      );

      const maze = JSON.parse(loadedboard.map);
      const name = JSON.parse(
        loadedboard.name
          .replace("[", '["')
          .replace("]", '"]')
          .replaceAll(", ", '", "')
      );
      const win = JSON.parse(loadedboard.win);

      const lose = JSON.parse(loadedboard.lose);
      const score = JSON.parse(loadedboard.score);
      const path = JSON.parse(loadedboard.path);
      setLoadGameCriteria((prev) => ({
        gameId: id,
        difficulty: difficulty,
        map: maze,
        win,
        lose,
        name,
        score,
        path,
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
        id={"carou4"}
        className={`${
          loadGameCriteria.gameId.length <= 1
            ? "overflow-hidden"
            : "overflow-scroll"
        } scroll-smooth hideScroll snap-x snap-mandatory py-10 w-[70rem] border-2 border-white text-white rounded-3xl backdrop-blur-xl`}
      >
        <div
          className={`${Comfor.className} z-[3] font-extrabold grid grid-rows-1 grid-flow-col gap-4`}
        >
          {loadGameCriteria.map.length > 0 ? (
            loadGameCriteria.map.map((theMap, index) => {
              return (
                <section
                  key={nanoid()}
                  className="snap-center w-[70rem] z-[3] font-extrabold flex flex-col justify-center place-items-center"
                >
                  <h1 className="text-3xl font-bold mb-8 uppercase">
                    {`${loadGameCriteria.name[index]} ( ${loadGameCriteria.difficulty[index]} )`}
                  </h1>
                  <div className="grid grid-rows-1 grid-flow-col">
                    {/* map */}
                    {/* <canvas
                      height="270px"
                      width="540px"
                      id={`canvas${index}`}
                      className="border-2 border-black bg-[url('/pixel_grass.jpg')] rounded-lg"
                    ></canvas> */}
                    <div className="bg-[url('/pixel_grass.jpg')] font-bold text-2xl h-[270px] w-[540px] rounded-3xl border-white border-2 flex flex-col gap-4 justify-center items-center">
                      <h1>Path : Path {loadGameCriteria.path[index]}</h1>
                      <h1>Win : {loadGameCriteria.win[index]}</h1>
                      <h1>Lose : {loadGameCriteria.lose[index]}</h1>
                      <h1>Score : {loadGameCriteria.score[index]}</h1>
                    </div>
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
                            handleLoadGame(loadGameCriteria.gameId[index]);
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
                      className="grid col-start-3 px-3 py-2 w-40 bg-red-300 text-black rounded-xl mt-10"
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
              className="snap-center w-[70rem] h-[18rem] z-[3] font-extrabold grid place-items-center text-3xl"
            >
              <h1>No Saved Game!</h1>
            </section>
          )}
        </div>
      </div>
    </div>
  );
}

"use client";

import { useEffect, useState } from "react";
import Treblecross from "./(component)/Treblecross";
import FFTicTacToe from "./(component)/FFTicTacToe";
import MisereTicTacToe from "./(component)/MisereTicTacToe";
import Swal from "sweetalert2";
import { useRouter } from "next/navigation";
import ImageNext from "next/image";
import NavBar from "../(components)/navBar";
import { nanoid } from "nanoid";

export default function page() {
  const router = useRouter();
  const [maze, setMaze] = useState([
    [9, 0, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 0, 0, 0, 1, 1, 1, 1],
    [0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1],
    [0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1],
    [0, 1, 0, 1, 1, 0, 0, 2, 0, 1, 0, 1, 0, 1, 1, 0, 0, 2, 0, 1],
    [0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1],
    [0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1],
    [0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1],
    [0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1],
    [0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1, 2, 1, 0, 0, 1],
    [0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1],
    [0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1],
    [0, 1, 1, 0, 0, 2, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 2, 0, 1],
    [0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0],
    [0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0],
    [0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0],
    [2, 0, 0, 0, 1, 1, 0, 0, 0, 1, 2, 0, 0, 0, 1, 1, 0, 0, 0, 1],
    [0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1],
    [0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1],
    [0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1],
    [0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1],
    [0, 0, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 0, 0, 0, 1, 1, 1, 1],
    [0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 2, 0, 0, 1, 1],
    [0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1],
    [0, 1, 0, 1, 1, 0, 0, 2, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1],
    [0, 1, 0, 1, 1, 2, 0, 1, 0, 1, 0, 1, 0, 1, 1, 2, 0, 0, 0, 1],
    [0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1],
    [0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1],
    [0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1],
    [0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1],
    [0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1],
    [0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1],
    [0, 1, 1, 0, 0, 2, 1, 1, 0, 1, 0, 1, 1, 0, 0, 2, 1, 1, 0, 1],
    [0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0],
    [0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0],
    [0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0],
    [2, 0, 0, 0, 1, 1, 0, 0, 0, 1, 2, 0, 0, 0, 1, 1, 0, 0, 0, 1],
    [0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1],
    [0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1],
    [1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 3],
  ]);
  const [difficulty, setDifficulty] = useState("");
  const [refresh, setRefresh] = useState(false);
  const [win, setWin] = useState(404);
  const [gameType, setGameType] = useState("");
  const [restart, setRestart] = useState(false);
  const [initialUpdate, setInitialUpdate] = useState(false);
  const [credentials, setCredentials] = useState({
    winNumber: 0,
    loseNumber: 0,
    score: 0.0,
    path: 0,
  });

  const [history, setHistory] = useState({
    difficulty: [""],
    win: [0],
    lose: [0],
    score: [0],
    path: [0],
    status: [0],
  });

  async function handleDeleteHistory() {
    const email = localStorage.getItem("email");
    await fetch(`http://localhost:8080/database/${email}/deleteCTDHistory`);
    setRefresh((prev) => !prev);
  }

  async function handleRestart() {
    const email = localStorage.getItem("email");
    await fetch(`http://localhost:8080/connectingthedots/${email}/restart`);
    const resMap = await fetch(
      `http://localhost:8080/connectingthedots/${email}/map`
    );
    const dataMap = await resMap.json();
    const map = JSON.parse(dataMap.Map);
    setMaze(map);
    setWin(404);
    setDifficulty("");
    setGameType("");
    setRestart((prev) => !prev);
    setCredentials({
      winNumber: 0,
      loseNumber: 0,
      score: 0,
      path: 0,
    });
  }

  async function handleBack() {
    const email = localStorage.getItem("email");

    await fetch(
      `http://localhost:8080/connectingthedots/${email}/takeBackStep`
    );

    const resMap = await fetch(
      `http://localhost:8080/connectingthedots/${email}/map`
    );
    const dataMap = await resMap.json();
    const map = JSON.parse(dataMap.Map);
    setMaze(map);
    setRefresh((prev) => !prev);
  }

  async function handleNext() {
    const email = localStorage.getItem("email");

    const res = await fetch(
      `http://localhost:8080/connectingthedots/${email}/move`
    );
    let data = await res.json();

    // unremark this to enable GOD MODE!!!!
    /* if (data === 1 || data === 2 || data === 3) {
      data = 2;
    } */

    const resMap = await fetch(
      `http://localhost:8080/connectingthedots/${email}/map`
    );
    const dataMap = await resMap.json();
    const map = JSON.parse(dataMap.Map);
    setMaze(map);

    if (data === 1) {
      (document.getElementById("nextBtn") as HTMLButtonElement).disabled = true;
      (document.getElementById("backBtn") as HTMLButtonElement).disabled = true;
      await Swal.fire({
        title: "You Arrived at a Station!",
        html: "Play <b>Treblecross</b> to Pass the Station!",
        timer: 2000,
        timerProgressBar: true,
        allowOutsideClick: false,
        background: "black",
        color: "white",
        backdrop: `
        url("/pokemon.gif")
        fixed
        center
        center
        / cover
        no-repeat
      `,
      });

      setGameType("Treblecross");
      (document.getElementById("Treblecross") as HTMLDivElement).style.display =
        "grid";
    } else if (data === 2) {
      (document.getElementById("nextBtn") as HTMLButtonElement).disabled = true;
      (document.getElementById("backBtn") as HTMLButtonElement).disabled = true;
      await Swal.fire({
        title: "You Arrived at a Station!",
        html: "Play <b>FFTicTacToe</b> to Pass the Station!",
        timer: 2000,
        timerProgressBar: true,
        allowOutsideClick: false,
        background: "black",
        color: "white",
        backdrop: `
        url("/pokemon.gif")
        fixed
        center
        center
        / cover
        no-repeat
      `,
      });
      setGameType("FFTicTacToe");
      (document.getElementById("FFTicTacToe") as HTMLDivElement).style.display =
        "grid";
    } else if (data === 3) {
      (document.getElementById("nextBtn") as HTMLButtonElement).disabled = true;
      (document.getElementById("backBtn") as HTMLButtonElement).disabled = true;
      await Swal.fire({
        title: "You Arrived at a Station!",
        html: "Play <b>MisereTicTacToe</b> to Pass the Station!",
        timer: 2000,
        timerProgressBar: true,
        allowOutsideClick: false,
        background: "black",
        color: "white",
        backdrop: `
        url("/pokemon.gif")
        fixed
        center
        center
        / cover
        no-repeat
      `,
      });
      setGameType("MisereTicTacToe");
      (
        document.getElementById("MisereTicTacToe") as HTMLDivElement
      ).style.display = "grid";
    } else if (data === 0) {
      Swal.fire({
        title: "You Win!",
        icon: "success",
        allowOutsideClick: false,
        confirmButtonText: "New Game?",
        showDenyButton: true,
        denyButtonText: "Back to Home?",
      }).then((result) => {
        if (result.isConfirmed) {
          handleRestart();
        } else if (result.isDenied) {
          router.push("/home");
        }
      });
    }
    setRefresh((prev) => !prev);
  }

  async function handleSave(name: string) {
    const email = localStorage.getItem("email");
    await fetch(
      `http://localhost:8080/connectingthedots/${email}/save/${name}`
    );
  }

  async function getDif() {
    const email = localStorage.getItem("email");
    const res = await fetch(
      `http://localhost:8080/connectingthedots/${email}/getdifficulty`
    );

    const dif = await res.json();

    if (dif.difficulty === "None") {
      const { value: diff } = await Swal.fire({
        title: "Difficulty",
        input: "select",
        allowOutsideClick: false,
        inputOptions: {
          Difficulty: {
            easy: "Easy",
            medium: "Medium",
            hard: "Hard",
          },
        },
        inputPlaceholder: "Select a difficulty",
        inputValidator: (value) => {
          return new Promise((resolve) => {
            if (value === "") {
              resolve("You need to select a difficulty!");
            } else {
              (async () => {
                await fetch(
                  `http://localhost:8080/connectingthedots/${email}/difficulty/${value}`
                );
              })();
              // @ts-ignore
              resolve();
            }
          });
        },
      });

      const { value: pathOption } = await Swal.fire({
        title: "Paths",
        input: "select",
        allowOutsideClick: false,
        inputOptions: {
          Paths: {
            0: "Path 1",
            1: "Path 2",
            2: "Path 3",
            3: "Path 4",
            4: "Path 5",
            5: "Path 6",
          },
        },
        inputPlaceholder: "Select a Path",
        inputValidator: (value) => {
          return new Promise((resolve) => {
            if (value === "") {
              resolve("You need to select a path!");
            } else {
              // @ts-ignore
              resolve();
            }
          });
        },
      });

      await fetch(
        `http://localhost:8080/connectingthedots/${email}/selectPath/${pathOption}`
      );
      setCredentials((prev) => ({
        ...prev,
        path: Number(pathOption) + 1,
      }));
      setDifficulty(diff);
    } else {
      setDifficulty(dif.difficulty);
      const resPath = await fetch(
        `http://localhost:8080/connectingthedots/${email}/getPathNumber`
      );

      const path = await resPath.json();

      setCredentials((prev) => ({
        ...prev,
        path: path.path,
      }));
    }
  }

  useEffect(() => {
    const email = localStorage.getItem("email");

    if (win !== 404) {
      (async () => {
        const actionCode = await fetch(
          `http://localhost:8080/connectingthedots/${email}/action/${win}`
        );

        const actionData = await actionCode.json();

        if (actionData === -1) {
          Swal.fire({
            title: "You Lose!",
            icon: "error",
            allowOutsideClick: false,
            confirmButtonText: "Restart?",
            showDenyButton: true,
            denyButtonText: "Back to Home?",
          }).then((result) => {
            if (result.isConfirmed) {
              handleRestart();
            } else if (result.isDenied) {
              router.push("/home");
            }
          });
        }

        const res = await fetch(
          `http://localhost:8080/connectingthedots/${email}/map`
        );
        const data = await res.json();
        const map = JSON.parse(data.Map);
        setMaze(map);
        setWin(404);
        (document.getElementById("nextBtn") as HTMLButtonElement).disabled =
          false;
        (document.getElementById("backBtn") as HTMLButtonElement).disabled =
          false;
      })();
    } else {
      (async () => {
        if (difficulty === "") {
          await getDif();
          const res = await fetch(
            `http://localhost:8080/connectingthedots/${email}/map`
          );
          const data = await res.json();
          const map = JSON.parse(data.Map);
          setMaze(map);
        }

        const resHistory = await fetch(
          `http://localhost:8080/database/${email}/getHistory`
        );

        const resHistoryData = await resHistory.json();

        setHistory({
          difficulty: JSON.parse(
            resHistoryData.difficulty
              .replace("[", '["')
              .replace("]", '"]')
              .replaceAll(", ", '", "')
          ),
          win: JSON.parse(resHistoryData.win),
          lose: JSON.parse(resHistoryData.lose),
          score: JSON.parse(resHistoryData.score),
          path: JSON.parse(resHistoryData.path),
          status: JSON.parse(resHistoryData.status),
        });
        console.log({
          difficulty: JSON.parse(
            resHistoryData.difficulty
              .replace("[", '["')
              .replace("]", '"]')
              .replaceAll(", ", '", "')
          ),
          win: JSON.parse(resHistoryData.win),
          lose: JSON.parse(resHistoryData.lose),
          score: JSON.parse(resHistoryData.score),
          path: JSON.parse(resHistoryData.path),
          status: JSON.parse(resHistoryData.status),
        });

        const resWin = await fetch(
          `http://localhost:8080/connectingthedots/${email}/getwin`
        );
        const resLose = await fetch(
          `http://localhost:8080/connectingthedots/${email}/getlose`
        );
        const resScore = await fetch(
          `http://localhost:8080/connectingthedots/${email}/getscore`
        );

        const dataWin = await resWin.json();
        const dataLose = await resLose.json();
        const dataScore = await resScore.json();

        setCredentials((prev) => ({
          ...prev,
          winNumber: dataWin.win,
          loseNumber: dataLose.lose,
          score: dataScore.score,
        }));

        const res = await fetch(
          `http://localhost:8080/connectingthedots/${email}/map`
        );
        const data = await res.json();
        const maze = JSON.parse(data.Map);
        setMaze(maze);

        const doorImage = new Image();
        doorImage.src = "/door.png";

        const suzume = new Image();
        suzume.src = "/suzume.png";

        const wall = new Image();
        wall.src = "/pixel_wall.png";

        const endpoint = new Image();
        endpoint.src = "/endpoint.png";
        const canvas = document.getElementById("canvas") as HTMLCanvasElement;

        const ctx = canvas.getContext("2d") as CanvasRenderingContext2D;
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        const numRows = maze.length;
        const numCols = maze[0].length;

        const cellWidth = canvas.width / numCols;
        const cellHeight = canvas.height / numRows;
        suzume.onload = () => {
          for (let i = 0; i < numRows; i++) {
            for (let j = 0; j < numCols; j++) {
              if (maze[i][j] === 9) {
                ctx.drawImage(
                  suzume,
                  cellWidth * j,
                  cellHeight * i,
                  cellWidth,
                  cellHeight
                );
              }
            }
          }
        };

        wall.onload = () => {
          for (let i = 0; i < numRows; i++) {
            for (let j = 0; j < numCols; j++) {
              if (maze[i][j] === 1) {
                ctx.drawImage(
                  wall,
                  cellWidth * j,
                  cellHeight * i,
                  cellWidth,
                  cellHeight
                );
              }
            }
          }
        };

        doorImage.onload = () => {
          for (let i = 0; i < numRows; i++) {
            for (let j = 0; j < numCols; j++) {
              if (maze[i][j] === 2) {
                ctx.drawImage(
                  doorImage,
                  cellWidth * j,
                  cellHeight * i,
                  cellWidth,
                  cellHeight
                );
              }
            }
          }
        };

        endpoint.onload = () => {
          for (let i = 0; i < numRows; i++) {
            for (let j = 0; j < numCols; j++) {
              if (maze[i][j] === 3) {
                ctx.drawImage(
                  endpoint,
                  cellWidth * j,
                  cellHeight * i,
                  cellWidth,
                  cellHeight
                );
              }
            }
          }
        };
      })();
    }

    setInitialUpdate(true);
  }, [initialUpdate, refresh, win, restart]);

  return (
    <>
      <ImageNext
        className="fixed z-[2]"
        src="/suz.jpeg"
        sizes="100vw"
        alt=""
        fill
      />
      <div className="w-full h-screen overflow-hidden bg-black opacity-30 z-[2] fixed"></div>
      <NavBar />
      <div className="relative z-[100] w-full h-screen overflow-hidden flex flex-col items-center justify-center gap-4 pt-[5rem]">
        <div className="flex items-center gap-10">
          <button
            onClick={handleBack}
            id="backBtn"
            className="z-[200] p-[0.5rem_1.5rem] font-bold text-white rounded-xl border-2 border-white backdrop-blur-xl"
          >
            Back
          </button>
          <h1 className="text-[2rem] text-white font-extrabold">
            Connecting the Dots
          </h1>
          <button
            onClick={handleNext}
            id="nextBtn"
            className="z-[200] p-[0.5rem_1.5rem] font-bold text-white rounded-xl border-2 border-white backdrop-blur-xl"
          >
            Next
          </button>
        </div>
        <div className="w-full flex gap-10 justify-center">
          <div className="text-white h-full w-[200px] rounded-xl backdrop-blur-xl border-2 border-black flex flex-col items-center justify-center">
            <h1>Win : {credentials.winNumber}</h1>
            <h1>Lose : {credentials.loseNumber}</h1>
            <h1>Score : {(Number(credentials.score) * 100).toFixed(2)}</h1>
            <h1>Path: Path {credentials.path}</h1>
            <h1>Difficulty: {difficulty.toUpperCase()}</h1>
          </div>
          <canvas
            height="540px"
            width="800px"
            id="canvas"
            className="border-2 border-black bg-[url('/pixel_grass.jpg')] rounded-lg"
          ></canvas>
          <div className="z-[999] relative text-white overflow-y-scroll h-[540px] w-[200px] rounded-xl backdrop-blur-xl border-2 border-black flex flex-col p-4">
            <h1 className="text-[1.5rem] text-center mb-4">History</h1>
            {history.difficulty[0] === "" ? (
              <div className="h-full w-full flex justify-center items-center">
                No History
              </div>
            ) : (
              (history.win as number[]).map((win, index) => {
                return (
                  <div
                    key={nanoid()}
                    className="text-sm p-4 border-2 rounded-xl mb-4 "
                  >
                    <h1>Win : {win}</h1>
                    <h1>Lose : {history.lose[index]}</h1>
                    <h1>
                      Score : {(Number(history.score[index]) * 100).toFixed(2)}
                    </h1>
                    <h1>Path: Path {history.path[index] + 1}</h1>
                    <h1>
                      Difficulty:{" "}
                      {(history.difficulty as string[])[index].toUpperCase()}
                    </h1>
                    <h1>
                      Status:{" "}
                      {Number(history.status[index]) === -1 ? "Lose" : "Win"}
                    </h1>
                  </div>
                );
              })
            )}
            <button
              onClick={handleDeleteHistory}
              className="border-2 rounded-lg"
            >
              Delete All
            </button>
          </div>
        </div>

        <div className="flex gap-10">
          <button
            onClick={() => {
              Swal.fire({
                title: "Are you sure you want to restart?",
                icon: "question",
                showDenyButton: true,
                confirmButtonText: "Yes",
                denyButtonText: "No",
                allowOutsideClick: false,
              }).then((result) => {
                if (result.isConfirmed) {
                  handleRestart();
                } else if (result.isDenied) {
                  return;
                }
              });
            }}
            className="z-[200] p-[0.5rem_1.5rem] font-bold text-white rounded-xl border-2 border-white backdrop-blur-xl w-[10rem]"
          >
            Restart
          </button>
          <button
            onClick={() => {
              Swal.fire({
                title: "Are you sure you want to save?",
                text: "You will be directed to home page!",
                icon: "question",
                showDenyButton: true,
                confirmButtonText: "Yes",
                denyButtonText: "No",
                allowOutsideClick: false,
              }).then((result) => {
                if (result.isConfirmed) {
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
                          handleSave(result.value);
                          router.push("/home");
                        }
                      } catch (err) {
                        console.log(err);
                      }
                    }
                  });
                } else if (result.isDenied) {
                }
              });
            }}
            className="z-[200] p-[0.5rem_1.5rem] font-bold text-white rounded-xl border-2 border-white backdrop-blur-xl w-[10rem]"
          >
            Save
          </button>
        </div>

        <div className="fixed top-0 left-0 right-0 bottom-0 grid place-items-center bg-transparent pt-[4.5rem]">
          <div
            className={`w-full h-screen ${
              gameType === "Treblecross" ? "block" : "hidden"
            } `}
          >
            <Treblecross setTicTacToe={setWin} difficulty={difficulty} />
          </div>

          <div
            className={`w-full h-screen ${
              gameType === "FFTicTacToe" ? "block" : "hidden"
            } `}
          >
            <FFTicTacToe setTicTacToe={setWin} difficulty={difficulty} />
          </div>

          <div
            className={`w-full h-screen ${
              gameType === "MisereTicTacToe" ? "block" : "hidden"
            } `}
          >
            <MisereTicTacToe setTicTacToe={setWin} difficulty={difficulty} />
          </div>
        </div>
      </div>
    </>
  );
}

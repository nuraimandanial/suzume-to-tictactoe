"use client";

import { useEffect, useState } from "react";
import Treblecross from "./(component)/Treblecross";
import FFTicTacToe from "./(component)/FFTicTacToe";
import MisereTicTacToe from "./(component)/MisereTicTacToe";
import Swal from "sweetalert2";
import { useRouter } from "next/navigation";
import ImageNext from "next/image";
import NavBar from "../(components)/navBar";

const player = {
  x: 0,
  y: 0,
};

const prevStep = {
  x: 0,
  y: 0,
};

let game = "";
let winCode = 0;

let maze = [
  [0, 0, 2, 0, 0, 0, 1, 1, 1, 1],
  [0, 1, 0, 1, 1, 0, 0, 0, 1, 1],
  [0, 1, 0, 1, 1, 0, 1, 0, 1, 1],
  [0, 1, 0, 1, 1, 0, 0, 2, 0, 1],
  [0, 1, 0, 1, 1, 2, 0, 1, 0, 1],
  [0, 1, 0, 1, 1, 0, 1, 1, 0, 1],
  [0, 1, 0, 0, 0, 0, 1, 1, 0, 1],
  [0, 1, 0, 1, 1, 1, 1, 1, 0, 1],
  [0, 1, 0, 1, 1, 1, 1, 0, 0, 1],
  [0, 0, 0, 1, 1, 1, 1, 0, 1, 1],
  [0, 1, 1, 1, 1, 0, 0, 0, 0, 1],
  [0, 1, 1, 0, 0, 2, 1, 1, 0, 1],
  [0, 1, 1, 0, 1, 0, 0, 1, 0, 0],
  [0, 1, 1, 0, 1, 1, 0, 1, 1, 0],
  [0, 1, 1, 0, 1, 1, 0, 1, 0, 0],
  [2, 0, 0, 0, 1, 1, 0, 0, 0, 1],
  [0, 1, 1, 0, 1, 1, 1, 1, 0, 1],
  [0, 1, 1, 0, 1, 1, 1, 1, 0, 1],
  [0, 0, 0, 0, 0, 0, 0, 0, 0, 1],
  [1, 1, 1, 1, 1, 1, 1, 1, 0, 3],
];

let station = ["Treblecross", "FFTicTacToe", "MisereTicTacToe"];
let difficulty = "";

export default function page() {
  const router = useRouter();
  const [win, setWin] = useState(0);
  const [gameType, setGameType] = useState(game);

  useEffect(() => {
    (async () => {
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
              resolve();
            }
          });
        },
      });

      difficulty = diff;
    })();
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

    const numRows = maze.length;
    const numCols = maze[0].length;

    const cellWidth = canvas.width / numCols;
    const cellHeight = canvas.height / numRows;
    suzume.onload = () => {
      ctx.drawImage(suzume, 0, 0, cellWidth, cellHeight);
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

    function draw(x: number, y: number) {
      ctx.drawImage(suzume, x, y, cellWidth, cellHeight);
    }

    function clear(x: number, y: number) {
      ctx.clearRect(x, y, cellWidth, cellHeight);
    }

    function arriveStation() {
      if (maze[player.y / cellHeight][player.x / cellWidth] === 2) {
        station = shuffleArray(station);
        game = station[0];
        setGameType(game);

        const element = document.getElementById(game);
        if (element) {
          element.style.display = "grid";
        }
      } else {
        game = "";
      }
    }

    function shuffleArray(array: Array<string>) {
      for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
      }
      return array;
    }

    function arriveEndPoint() {
      if (maze[player.y / cellHeight][player.x / cellWidth] === 3) {
        Swal.fire({
          title: "Congratulation! You Win!",
          allowOutsideClick: false,
        }).then((value) => {
          if (value.isConfirmed) {
            Swal.fire({
              title: "Play Again?",
              showConfirmButton: true,
              showDenyButton: true,
              confirmButtonText: "Yes",
              denyButtonText: "No",
              allowOutsideClick: false,
            }).then((value) => {
              if (value.isConfirmed) {
                window.location.reload();
              } else if (value.isDenied) {
                router.push("/home");
              }
            });
          }
        });
      }
    }

    const handleKeyDown = (event: KeyboardEvent) => {
      switch (event.key) {
        case "ArrowUp":
          if (
            !(player.y - cellHeight < 0) &&
            maze[(player.y - cellHeight) / cellHeight][player.x / cellWidth] !==
              1 &&
            game === "" &&
            winCode === 0
          ) {
            prevStep.x = player.x;
            prevStep.y = player.y;
            clear(player.x, player.y);
            player.y -= cellHeight;
            arriveStation();
            draw(player.x, player.y);
          } else if (
            !(player.y - cellHeight < 0) &&
            maze[(player.y - cellHeight) / cellHeight][player.x / cellWidth] !==
              1 &&
            winCode === 1
          ) {
            clear(player.x, player.y);
            player.y -= cellHeight;
            draw(player.x, player.y);
            winCode = 0;
            game = "";
          }
          arriveEndPoint();
          break;
        case "ArrowDown":
          if (
            player.y + cellHeight < canvas.height &&
            maze[(player.y + cellHeight) / cellHeight][player.x / cellWidth] !==
              1 &&
            game === "" &&
            winCode === 0
          ) {
            prevStep.x = player.x;
            prevStep.y = player.y;
            clear(player.x, player.y);
            player.y += cellHeight;
            arriveStation();
            draw(player.x, player.y);
          } else if (
            player.y + cellHeight < canvas.height &&
            maze[(player.y + cellHeight) / cellHeight][player.x / cellWidth] !==
              1 &&
            winCode === 1
          ) {
            clear(player.x, player.y);
            player.y += cellHeight;
            draw(player.x, player.y);
            winCode = 0;
            game = "";
          }
          arriveEndPoint();
          break;
        case "ArrowLeft":
          if (
            !(player.x - cellWidth < 0) &&
            maze[player.y / cellHeight][(player.x - cellWidth) / cellWidth] !==
              1 &&
            game === "" &&
            winCode === 0
          ) {
            prevStep.x = player.x;
            prevStep.y = player.y;
            clear(player.x, player.y);
            player.x -= cellWidth;
            arriveStation();
            draw(player.x, player.y);
          } else if (
            !(player.x - cellWidth < 0) &&
            maze[player.y / cellHeight][(player.x - cellWidth) / cellWidth] !==
              1 &&
            winCode === 1
          ) {
            clear(player.x, player.y);
            player.x -= cellWidth;
            draw(player.x, player.y);
            winCode = 0;
            game = "";
          }
          arriveEndPoint();
          break;
        case "ArrowRight":
          if (
            player.x + cellWidth < canvas.width &&
            maze[player.y / cellHeight][(player.x + cellWidth) / cellWidth] !==
              1 &&
            game === "" &&
            winCode === 0
          ) {
            prevStep.x = player.x;
            prevStep.y = player.y;
            clear(player.x, player.y);
            player.x += cellWidth;
            arriveStation();
            draw(player.x, player.y);
          } else if (
            player.x + cellWidth < canvas.width &&
            maze[player.y / cellHeight][(player.x + cellWidth) / cellWidth] !==
              1 &&
            winCode === 1
          ) {
            clear(player.x, player.y);
            player.x += cellWidth;
            draw(player.x, player.y);
            winCode = 0;
            game = "";
          }
          arriveEndPoint();
          break;
      }
    };
    window.addEventListener("keydown", handleKeyDown);
    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, []);

  useEffect(() => {
    const canvas = document.getElementById("canvas") as HTMLCanvasElement;

    const ctx = canvas.getContext("2d") as CanvasRenderingContext2D;

    const numRows = maze.length;
    const numCols = maze[0].length;

    const cellWidth = canvas.width / numCols;
    const cellHeight = canvas.height / numRows;

    const suzume = new Image();
    suzume.src = "/suzume.png";

    const doorImage = new Image();
    doorImage.src = "/door.png";

    function draw(x: number, y: number) {
      suzume.onload = () => {
        ctx.drawImage(suzume, x, y, cellWidth, cellHeight);
      };
    }

    function clear(x: number, y: number) {
      ctx.clearRect(x, y, cellWidth, cellHeight);
    }

    if (win === 1) {
      winCode = 1;
      clear(player.x, player.y);
      draw(player.x, player.y);
      const stationFiltered = station.filter((element) => element !== game);
      station = stationFiltered;
      maze[player.y / cellHeight][player.x / cellWidth] = 0;
      setWin(0);
    } else if (win === -1) {
      clear(player.x, player.y);

      ctx.drawImage(doorImage, player.x, player.y, cellWidth, cellHeight);

      draw(prevStep.x, prevStep.y);
      player.x = prevStep.x;
      player.y = prevStep.y;
      setWin(0);
      winCode = 0;
      game = "";
    }
  }, [win]);

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
        <h1 className="text-[2rem] text-white font-extrabold">
          Connecting the Dots
        </h1>

        <canvas
          height="640px"
          width="640px"
          id="canvas"
          className="border-2 border-black bg-[url('/pixel_grass.jpg')] rounded-lg"
        ></canvas>
        <div className="fixed top-0 left-0 right-0 bottom-0 grid place-items-center bg-transparent pt-[4.5rem]">
          <div
            className={`w-full h-screen ${
              gameType === "Treblecross" ? "" : "hidden"
            } `}
          >
            <Treblecross setTicTacToe={setWin} difficulty={difficulty} />
          </div>

          <div
            className={`w-full h-screen ${
              gameType === "FFTicTacToe" ? "" : "hidden"
            } `}
          >
            <FFTicTacToe setTicTacToe={setWin} difficulty={difficulty} />
          </div>

          <div
            className={`w-full h-screen ${
              gameType === "MisereTicTacToe" ? "" : "hidden"
            } `}
          >
            <MisereTicTacToe setTicTacToe={setWin} difficulty={difficulty} />
          </div>
        </div>
      </div>
    </>
  );
}

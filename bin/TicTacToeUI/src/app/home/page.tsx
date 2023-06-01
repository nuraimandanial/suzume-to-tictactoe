"use client";
import { useEffect } from "react";
import TTT from "./(components)/ttt";
import { useRouter } from "next/navigation";
import Image from "next/image";
import NavBar from "../(components)/navBar";
import FFTTT from "./(components)/ffttt";
import MTTT from "./(components)/mttt";
import Background from "../(components)/Background";
import { nanoid } from "nanoid";

export default function page() {
  const router = useRouter();

  const token = window.localStorage.getItem("token");

  if (!token) {
    router.push("/login");
  }

  const items = [
    {
      ID: [2, 1, 3],
      GameName: "3 x 3 Regular Tic Tac Toe",
      Page: <TTT />,
    },
    {
      ID: [3, 2, 4],
      GameName: "5 x 5 Regular Tic Tac Toe",
      Page: <FFTTT />,
    },
    {
      ID: [4, 1],
      GameName: "Misere Regular Tic Tac Toe",
      Page: <MTTT />,
    },
  ];

  useEffect(() => {
    (async () => {
      const email = window.localStorage.getItem("email");
      const res = await fetch(`http://localhost:8080/${email}/newGame`);
    })();
  }, []);

  function nextPage(page: string) {
    const element = document.getElementById(page);
    element?.scrollIntoView({ behavior: "smooth" });
  }

  async function handleNewGame(route: string, apiRoute: string, email: string) {
    const res = await fetch(
      `http://localhost:8080${apiRoute}/${email}/newGame`
    );
    if (res.ok) {
      router.push(`/${route}`);
      return "New Game Started!";
    }
  }
  return (
    <div className="snap-y snap-mandatory h-screen overflow-y-scroll bg-black">
      <NavBar />
      <Background />
      <section
        id="page1"
        className="relative snap-center h-screen w-full flex flex-col justify-evenly items-center"
      >
        <div
          onClick={() => {
            nextPage("page2");
          }}
          className="cursor-pointer absolute w-20 h-20 rounded-full border-2 border-white right-10 bottom-10 animate-bounce grid place-items-center text-3xl text-white"
        >
          <Image
            className="invert"
            src="/pngtree-down-arrow-png-image_4556532-removebg-preview.png"
            height="50"
            width="50"
            alt=""
          />
        </div>
        <div className="z-[99] w-full flex justify-center items-center">
          <h1 className="animate-pulse font-bold text-5xl text-white">
            {" "}
            Start a New Game ?
          </h1>
        </div>

        <div className="z-[99] h-[30rem] w-[30rem] border-2 border-white backdrop-blur-xl bg-transparent rounded-2xl flex flex-col justify-evenly items-center">
          <button
            onClick={() => {
              const email = window.localStorage.getItem("email");
              if (email) {
                handleNewGame("tictactoe", "", email);
              }
            }}
            className="z-[100] text-white text-[1.5rem] w-[65%] border-2 border-white p-6 rounded-md"
          >
            3 x 3 Regular <br /> Tic-Tac-Toe
          </button>
          <button
            onClick={() => {
              const email = window.localStorage.getItem("email");
              if (email) {
                handleNewGame("tictactoe/ffttt", "/fftictactoe", email);
              }
            }}
            className="z-[100] text-white text-[1.5rem] w-[65%] border-2 border-white p-6 rounded-md"
          >
            5 x 5 Regular <br /> Tic-Tac-Toe
          </button>
          <button
            onClick={() => {
              const email = window.localStorage.getItem("email");
              if (email) {
                handleNewGame("tictactoe/mttt", "/mtictactoe", email);
              }
            }}
            className="z-[100] text-white text-[1.5rem] border-2 w-[65%] border-white p-6 rounded-md"
          >
            Misere Tic-Tac-Toc
          </button>
        </div>
      </section>
      {items.map((item, index) => {
        return (
          <section
            key={nanoid()}
            id={`page${item.ID[0]}`}
            className="relative snap-center h-screen w-full flex flex-col justify-evenly items-center"
          >
            <div
              onClick={() => {
                nextPage(`page${item.ID[1]}`);
              }}
              className="cursor-pointer absolute w-20 h-20 rounded-full border-2 border-white left-10 bottom-10 animate-bounce grid place-items-center text-3xl text-white"
            >
              <Image
                className="invert rotate-180"
                src="/pngtree-down-arrow-png-image_4556532-removebg-preview.png"
                height="50"
                width="50"
                alt=""
              />
            </div>
            {!(index === 2) && (
              <div
                onClick={() => {
                  nextPage(`page${item.ID[2]}`);
                }}
                className="cursor-pointer absolute w-20 h-20 rounded-full border-2 border-white right-10 bottom-10 animate-bounce grid place-items-center text-3xl text-white"
              >
                <Image
                  className="invert"
                  src="/pngtree-down-arrow-png-image_4556532-removebg-preview.png"
                  height="50"
                  width="50"
                  alt=""
                />
              </div>
            )}
            <div className="z-[99] w-full flex justify-center items-center">
              <h1 className="animate-pulse font-bold text-5xl text-white">
                {`Continue Your ${item.GameName} ?`}
              </h1>
            </div>

            <div className="flex justify-center items-cente">{item.Page}</div>
          </section>
        );
      })}
    </div>
  );
}

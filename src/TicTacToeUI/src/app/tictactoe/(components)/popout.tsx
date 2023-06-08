"use client";

import { useState } from "react";
import { Comfortaa } from "next/font/google";
const Comfor = Comfortaa({
  weight: ["400", "500", "600", "700"],
  subsets: ["latin", "latin-ext"],
});
export default function Popout({
  status,
  gameType,
}: {
  status: number;
  gameType: string;
}) {
  const [show, setShow] = useState(true);

  function handleClick() {
    setShow((prev) => !prev);
  }

  function messageDisplay() {
    if (gameType === "PvP") {
      return status === 1
        ? "Player 1 Win!"
        : status === -1
        ? "Player 2 Win!"
        : status === 0
        ? "Tie!"
        : "";
    } else if (gameType === "PvPC") {
      return status === 1
        ? "You Win!"
        : status === -1
        ? "You Lose!"
        : status === 0
        ? "Tie!"
        : "";
    } else {
      return status === 1
        ? "Computer 1 Win!"
        : status === -1
        ? "Computer 2 Win!"
        : "Tie!";
    }
  }

  const message = messageDisplay();
  return (
    <>
      {show && (
        <div className="fixed z-[99] w-[25rem] h-[25rem] bg-white rounded-2xl flex justify-center items-center">
          <button
            onClick={handleClick}
            className="absolute grid place-content-center p-[6px] w-10 h-10 right-2 top-2 text-[2rem] text-black"
          >
            <h1 className={`${Comfor.className} text-red-500`}>X</h1>
          </button>
          <h1 className="text-5xl font-bold w-full text-center">{message}</h1>
        </div>
      )}
    </>
  );
}

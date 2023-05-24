"use client";

import { useState } from "react";
import { Comfortaa } from "next/font/google";
const Comfor = Comfortaa({
  weight: ["400", "500", "600", "700"],
  subsets: ["latin", "latin-ext"],
});
export default function Popout({ status }: { status: number }) {
  const [show, setShow] = useState(true);

  function handleClick() {
    setShow((prev) => !prev);
  }
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
          <h1 className="text-5xl font-bold">
            {status === 1
              ? "You Win!"
              : status === -1
              ? "You Lose!"
              : status === 0
              ? "Tie!"
              : ""}
          </h1>
        </div>
      )}
    </>
  );
}

"use client";

import Image from "next/image";
import { useState } from "react";

export default function PlayMusic() {
  const [play, setPlay] = useState(false);
  function handlePlay(play: boolean) {
    const element = document.getElementById("audioClass") as HTMLAudioElement;
    if (element) {
      if (play) {
        element.pause();
        setPlay((prev) => !prev);
      } else {
        element.play();
        setPlay((prev) => !prev);
      }
    }
  }
  return (
    <div className="z-[1000] fixed top-5 h-[3rem] w-[3rem] rounded-full bg-white grid place-items-center">
      <div className="absolute h-[3rem] w-[3rem] rounded-full bg-cyan-300 animate-[ping_2.5s_ease-in-out_infinite]"></div>
      <div className="absolute h-[3rem] w-[3rem] rounded-full bg-cyan-200 animate-[ping_2s_ease-in-out_infinite]"></div>
      <div className="absolute h-[3rem] w-[3rem] rounded-full bg-cyan-100 animate-[ping_1.5s_ease-in-out_infinite]"></div>
      <div
        onClick={() => handlePlay(play)}
        className="cursor-pointer relative w-[1.3rem] h-[1.3rem] rounded-full"
      >
        <Image
          src={`${play ? "/pause.png" : "/play-button-arrowhead.png"}`}
          fill
          priority
          sizes="10vw"
          alt=""
        />
      </div>
    </div>
  );
}

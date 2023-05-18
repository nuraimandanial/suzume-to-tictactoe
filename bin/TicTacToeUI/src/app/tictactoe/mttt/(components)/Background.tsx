import React from "react";

export default function Background() {
  return (
    <>
      <div className="absolute h-screen w-full bg-black opacity-50 z-[2]"></div>
      <video
        className="absolute bg-transparent h-full w-full object-fill"
        autoPlay
        muted
        loop
      >
        <source
          src="/Y2Mate.is - Suzume No Tojimari  Wallpaper-bIbJxI29w3Q-1080p-1654940366592.mp4"
          type="video/mp4"
        />
      </video>
    </>
  );
}

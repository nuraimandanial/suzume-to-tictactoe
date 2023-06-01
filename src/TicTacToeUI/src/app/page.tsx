"use client";

import { useRouter } from "next/navigation";
import { Poppins } from "next/font/google";
import { motion } from "framer-motion";
import { useEffect } from "react";

const popins = Poppins({
  weight: ["100", "200", "300", "400", "500", "600", "700", "800", "900"],
  subsets: ["latin"],
});
export default function Home() {
  window.localStorage.removeItem("difficulty");
  const router = useRouter();

  return (
    <motion.div className={`${popins.className} h-screen w-full`}>
      <motion.div className="absolute z-[99] h-screen w-full text-center grid place-items-center text-[3rem] font-extrabold gradientAnimation ">
        <div className="w-[80%] flex flex-col justify-center items-center">
          <h1>Welcome to Suzume's Tic Tac Toe!</h1>
          <h1 className="text-[1.5rem] gradientAnimation2">
            Play Tic Tac Toe and Save the World!
          </h1>
          <button
            onClick={() => {
              router.push("/login");
            }}
            className="gradientAnimation-border mt-8 backdrop-blur-md w-[10rem] h-[4rem] p-[1rem_2rem] rounded-full flex justify-center items-center border-4"
          >
            <h1 className="text-[1.5rem] text-white">Login</h1>
          </button>
        </div>
      </motion.div>

      <video
        className="absolute bg-transparent h-full w-full object-fill"
        autoPlay
        muted
        loop
      >
        <source
          src="/Y2Mate.is - Suzume no Tojimari  Anime wallpaper-Fn828XbvOog-1080p-1659428958319.mp4"
          type="video/mp4"
        />
      </video>

      <div className="absolute h-screen w-full bg-black opacity-70"></div>
    </motion.div>
  );
}

"use client";
import Image from "next/image";
import { useRouter } from "next/navigation";
import TypewriterComponent from "typewriter-effect";
import { Poppins } from "next/font/google";
import { motion } from "framer-motion";

const popins = Poppins({
  weight: ["100", "200", "300", "400", "500", "600", "700", "800", "900"],
  subsets: ["latin"],
});
export default function Home() {
  window.localStorage.removeItem("difficulty");
  const router = useRouter();

  return (
    <div className={`${popins.className} h-screen w-full`}>
      <motion.div
        className="absolute z-[99] h-screen w-full text-center grid place-items-center text-[3rem] font-extrabold gradientAnimation "
        animate={{ y: -200, transition: { delay: 7, duration: 3 } }}
      >
        <div className="w-[80%]">
          <TypewriterComponent
            onInit={(typewriter) => {
              typewriter.typeString("Welcome to Suzume's Tic Tac Toe!").start();
            }}
          />
        </div>
      </motion.div>
      <motion.div
        className="absolute bottom-[10rem] z-[100] h-[15rem] w-full flex justify-evenly items-center text-white text-[2rem] font-extrabold"
        initial={{ display: "none" }}
        animate={{ display: "flex", transition: { delay: 9, duration: 1 } }}
      >
        <button
          onClick={() => {
            router.push("/login");
          }}
          className="w-1/2 h-[15rem] gradientAnimation cursor-pointer hover:scale-[1.1] hover:backdrop-blur-xl transition duration-1000"
        >
          Login
        </button>
        <div className="h-[15rem] w-1 bg-white"></div>
        <button
          onClick={() => {
            router.push("/login");
          }}
          className="w-1/2 h-[15rem] gradientAnimation cursor-pointer hover:scale-[1.1] hover:backdrop-blur-xl transition duration-1000"
        >
          Sign Up
        </button>
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
    </div>
  );
}

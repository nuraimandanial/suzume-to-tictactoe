"use client";

import { useEffect, useState } from "react";
import TypewriterComponent from "typewriter-effect";
import { motion, useAnimate } from "framer-motion";
import LoginPage from "../(components)/LoginPage";
import SignUpPage from "../(components)/SignUpPage";
import { useRouter } from "next/navigation";

export default function page() {
  const router = useRouter();
  useEffect(() => {
    const token = window.localStorage.getItem("token");
    const email = window.localStorage.getItem("email");
    const username = window.localStorage.getItem("username");

    if (token && email && username) {
      router.push("/home");
    }
  }, []);

  const [scope, animate] = useAnimate();
  const [scope2, animate2] = useAnimate();

  const [login, setLogin] = useState(true);

  return (
    <div className="h-screen w-full flex justify-center items-center">
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
      <div className="flex flex-[1fr_1fr] w-full">
        <motion.div ref={scope} className="gradientBG relative w-full z-[999]">
          {login ? <LoginPage /> : <SignUpPage />}
          <button
            onClick={() => {
              setLogin((prev) => !prev);
              animate2(
                scope2.current,
                { x: login ? "-100%" : 0 },
                { duration: 1 }
              );
              animate(
                scope.current,
                { x: login ? "100%" : 0 },
                { duration: 1 }
              );
            }}
            className={`absolute text-gray-600 w-full ${
              login ? "bottom-[15rem]" : "bottom-[13rem]"
            } text-center text-xs z-[999]`}
          >
            {login ? "Don't have an account?" : "Already have an account?"}
          </button>
        </motion.div>

        <motion.div
          ref={scope2}
          className="w-full bg-transparent flex justify-center items-center"
        >
          <div
            className={`${
              login ? "" : "hidden"
            } absolute text-3xl gradientAnimation z-[99] font-extrabold`}
          >
            <TypewriterComponent
              onInit={(typewriter) => {
                typewriter
                  .typeString("Login and Continue Your Journey!")
                  .start();
              }}
            />
          </div>
          <div
            className={`${
              login ? "hidden" : ""
            } absolute text-3xl gradientAnimation z-[99] font-extrabold`}
          >
            <TypewriterComponent
              onInit={(typewriter) => {
                typewriter
                  .typeString("Join Us and Enjoy Your Journey!")
                  .start();
              }}
            />
          </div>
        </motion.div>
      </div>
    </div>
  );
}

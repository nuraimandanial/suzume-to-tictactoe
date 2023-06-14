"use client";
import { useRouter } from "next/navigation";
import NavBar from "../(components)/navBar";
import Background from "./(components)/Background";
import FFtictactoe from "./(components)/FFtictactoe";
import Mtictactoe from "./(components)/Mtictactoe";
import RegularTTT from "./(components)/treblecrossTicTacToe";
import StoryMode from "./(components)/storymode";

export default function page() {
  const token = window.localStorage.getItem("token");
  const router = useRouter();
  if (!token) {
    router.push("/login");
  }
  return (
    <div className="overflow-y-scroll snap-mandatory snap-y h-screen w-full">
      <NavBar />
      <Background />
      <StoryMode />
      <RegularTTT />
      <FFtictactoe />
      <Mtictactoe />
    </div>
  );
}

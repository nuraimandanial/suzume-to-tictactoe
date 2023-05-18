"use client";
import NavBar from "../(components)/navBar";
import Background from "./(components)/Background";
import FFtictactoe from "./(components)/FFtictactoe";
import Mtictactoe from "./(components)/Mtictactoe";
import RegularTTT from "./(components)/regularTTT";

export default function page() {
  return (
    <div className="overflow-y-scroll snap-mandatory snap-y h-screen w-full">
      <NavBar />
      <Background />
      <RegularTTT />
      <FFtictactoe />
      <Mtictactoe />
    </div>
  );
}

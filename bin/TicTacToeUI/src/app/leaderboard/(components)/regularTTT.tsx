"use client";
import { useState, useEffect } from "react";
import { nanoid } from "nanoid";
import { useRouter } from "next/navigation";
import ScrollButton from "./ScrollButton";
import ExtractLeaderBoardClass from "./ExtractLeaderBoardClass";

export default function RegularTTT() {
  const router = useRouter();
  const [TTT, setTTT] = useState({
    EasyUserName: [""],
    EasyWinLoseRatio: [0.0],
    MediumUserName: [""],
    MediumWinLoseRatio: [0.0],
    HardUserName: [""],
    HardWinLoseRatio: [0.0],
  });

  const items = [
    [0, "Easy", TTT.EasyUserName, TTT.EasyWinLoseRatio],
    [1, "Medium", TTT.MediumUserName, TTT.MediumWinLoseRatio],
    [2, "Hard", TTT.HardUserName, TTT.HardWinLoseRatio],
  ];

  useEffect(() => {
    const token = window.localStorage.getItem("token");
    if (!token) {
      router.push("/login");
    }

    (async () => {
      const obj = new ExtractLeaderBoardClass();
      const data = await obj.ExtractLeaderBoard("tictactoe", "easy");
      const data2 = await obj.ExtractLeaderBoard("tictactoe", "medium");
      const data3 = await obj.ExtractLeaderBoard("tictactoe", "hard");
      setTTT({
        EasyUserName: data?.userName,
        EasyWinLoseRatio: data?.winLoseRatio,
        MediumUserName: data2?.userName,
        MediumWinLoseRatio: data2?.winLoseRatio,
        HardUserName: data3?.userName,
        HardWinLoseRatio: data3?.winLoseRatio,
      });
    })();
  }, []);

  return (
    <section
      id="leaderboardpage1"
      className="relative snap-center h-screen w-full flex flex-col justify-center items-center gap-14 py-2"
    >
      <ScrollButton boardID="leaderboardcarou" />
      <h1 className="z-[99] text-white text-4xl font-extrabold">
        3 x 3 Regular Tic Tac Toe
      </h1>
      <div
        id="leaderboardcarou"
        className="scroll-smooth overflow-x-scroll snap-mandatory snap-x z-[100] h-[70%] w-[50rem] border-2 border-white bg-transparent backdrop-blur-xl rounded-xl grid grid-rows-1 grid-flow-col"
      >
        {items.map((item) => {
          return (
            <section
              key={nanoid()}
              id={`${item[0]}`}
              className="snap-center py-10 h-full w-[50rem] flex flex-col justify-center items-center gap-10"
            >
              <h1 className="text-white text-3xl font-extrabold">{`${item[1]}`}</h1>
              {(item[3] as number[]).length === 0 ? (
                <div className="h-full w-full grid place-items-center border-y-2">
                  {" "}
                  <h1 className="text-5xl text-white">No Records!</h1>
                </div>
              ) : (
                <div className="h-full w-full grid grid-cols-2 grid-flow-col-dense border-y-2 py-8">
                  {(item[2] as string[]).map((userName, index) => {
                    if (index < 10) {
                      return (
                        <div
                          key={nanoid()}
                          className="h-[10%] pb-8 border-white col-start-1 text-white font-bold text-md flex justify-center gap-14"
                        >
                          <h1 className="">{`${index + 1})`}</h1>
                          {userName}
                        </div>
                      );
                    } else return "";
                  })}
                  {(item[3] as number[]).map((winLoseRatio, index) => {
                    if (index < 10) {
                      return (
                        <div
                          key={nanoid()}
                          className="h-[10%] pb-8 border-white col-start-2 text-white font-bold text-md flex justify-center"
                        >
                          {winLoseRatio.toFixed(2)}
                        </div>
                      );
                    } else return "";
                  })}
                </div>
              )}
            </section>
          );
        })}
      </div>
    </section>
  );
}

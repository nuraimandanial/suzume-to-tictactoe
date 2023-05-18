"use client";
import { useState, useEffect } from "react";
import { nanoid } from "nanoid";
import ScrollButton from "./ScrollButton";
import ExtractLeaderBoardClass from "./ExtractLeaderBoardClass";

export default function Mtictactoe() {
  const [MTTT, setMTTTT] = useState({
    EasyUserName: [""],
    EasyWinLoseRatio: [0.0],
    MediumUserName: [""],
    MediumWinLoseRatio: [0.0],
    HardUserName: [""],
    HardWinLoseRatio: [0.0],
  });

  const items = [
    [6, "Easy", MTTT.EasyUserName, MTTT.EasyWinLoseRatio],
    [7, "Medium", MTTT.MediumUserName, MTTT.MediumWinLoseRatio],
    [8, "Hard", MTTT.HardUserName, MTTT.HardWinLoseRatio],
  ];

  useEffect(() => {
    (async () => {
      const obj = new ExtractLeaderBoardClass();
      const data = await obj.ExtractLeaderBoard("mttt", "easy");
      const data2 = await obj.ExtractLeaderBoard("mttt", "medium");
      const data3 = await obj.ExtractLeaderBoard("mttt", "hard");
      setMTTTT({
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
      id="leaderboardpage3"
      className="relative snap-center h-screen w-full flex flex-col justify-center items-center gap-14 py-2"
    >
      <ScrollButton boardID="leaderboardcarou3" />
      <h1 className="z-[100] text-white text-4xl font-extrabold">
        Misere Tic Tac Toe
      </h1>
      <div
        id="leaderboardcarou3"
        className="scroll-smooth overflow-x-scroll snap-mandatory snap-x z-[100] h-[70%] w-[50rem] border-2 border-white bg-transparent backdrop-blur-xl rounded-xl grid grid-rows-1 grid-flow-col"
      >
        {items.map((item) => {
          return (
            <section
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

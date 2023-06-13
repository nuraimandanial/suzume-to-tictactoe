"use client";
import { useState, useEffect } from "react";
import { nanoid } from "nanoid";
import ScrollButton from "./ScrollButton";
import ExtractLeaderBoardClass from "./ExtractLeaderBoardClass";

export default function Mtictactoe() {
  const [MTTT, setMTTT] = useState({
    EasyUserName: [""],
    EasyScore: [0],
    EasyWin: [0],
    EasyLose: [0],
    MediumUserName: [""],
    MediumScore: [0],
    MediumWin: [0],
    MediumLose: [0],
    HardUserName: [""],
    HardScore: [0],
    HardWin: [0],
    HardLose: [0],
  });

  const items = [
    [6, "Easy", MTTT.EasyUserName, MTTT.EasyScore, MTTT.EasyWin, MTTT.EasyLose],
    [
      7,
      "Medium",
      MTTT.MediumUserName,
      MTTT.MediumScore,
      MTTT.MediumWin,
      MTTT.MediumLose,
    ],
    [8, "Hard", MTTT.HardUserName, MTTT.HardScore, MTTT.HardWin, MTTT.HardLose],
  ];

  useEffect(() => {
    (async () => {
      const obj = new ExtractLeaderBoardClass();
      const data = await obj.ExtractLeaderBoard("mttt", "easy");
      const data2 = await obj.ExtractLeaderBoard("mttt", "medium");
      const data3 = await obj.ExtractLeaderBoard("mttt", "hard");
      setMTTT({
        EasyUserName: data?.userName,
        EasyScore: data?.score,
        EasyWin: data?.win,
        EasyLose: data?.lose,
        MediumUserName: data2?.userName,
        MediumScore: data2?.score,
        MediumWin: data2?.win,
        MediumLose: data2?.lose,
        HardUserName: data3?.userName,
        HardScore: data3?.score,
        HardWin: data3?.win,
        HardLose: data3?.lose,
      });
    })();
  }, []);

  return (
    <section
      id="leaderboardpage3"
      className="pt-10 relative snap-center h-screen w-full flex flex-col justify-center items-center gap-14 py-2"
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
              key={nanoid()}
              id={`${item[0]}`}
              className="snap-center px-10 py-10 h-full w-[50rem] flex flex-col justify-between items-center gap-10"
            >
              <h1 className="text-center font-extrabold text-2xl text-white">
                {item[1]}
              </h1>
              <div className="h-[90%] w-full relative overflow-x-auto shadow-md sm:rounded-lg">
                {(item[2] as string[])[0] === "" ? (
                  <div className="w-full h-full grid place-items-center border-t-4 border-white">
                    <h1 className="text-center font-extrabold text-4xl text-white">
                      No Records!
                    </h1>
                  </div>
                ) : (
                  <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
                    <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                      <tr>
                        <th scope="col" className="px-6 py-3">
                          No.
                        </th>
                        <th scope="col" className="px-6 py-3">
                          User
                        </th>
                        <th scope="col" className="px-6 py-3">
                          Win
                        </th>
                        <th scope="col" className="px-6 py-3">
                          Lose
                        </th>
                        <th scope="col" className="px-6 py-3">
                          Score
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {(item[2] as string[]).map((item2, index) => {
                        return (
                          <tr
                            key={nanoid()}
                            className="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600"
                          >
                            <th
                              scope="row"
                              className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
                            >
                              {index + 1}
                            </th>

                            <td className="px-6 py-4 dark:text-white">
                              {(item[2] as string[])[index]}
                            </td>
                            <td className="px-6 py-4 dark:text-white">
                              {(item[4] as number[])[index]}
                            </td>
                            <td className="px-6 py-4 dark:text-white">
                              {(item[5] as number[])[index]}
                            </td>
                            <td className="px-6 py-4 dark:text-white">
                              {(item[3] as number[])[index].toFixed(0)}
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                )}
              </div>
            </section>
          );
        })}
      </div>
    </section>
  );
}

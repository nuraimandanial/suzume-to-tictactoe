"use client";

import Image from "next/image";

export default function BottomNav({
  gameType,
  setGameType,
}: {
  gameType: string;
  setGameType: Function;
}) {
  function handleChange(game: string) {
    setGameType(game);
  }

  return (
    <div className="fixed z-50 w-full h-16 max-w-lg -translate-x-1/2 bg-white border border-gray-200 rounded-full bottom-4 left-1/2 dark:bg-gray-700 dark:border-gray-600">
      <div className="grid h-full max-w-lg grid-cols-3 mx-auto">
        {/* P vs PC */}

        <button
          type="button"
          onClick={() => handleChange("PvPC")}
          className={`${
            gameType === "PvPC"
              ? "bg-gray-50 dark:bg-gray-800"
              : "hover:bg-gray-50 dark:hover:bg-gray-800"
          } inline-flex flex-col items-center justify-center px-5 rounded-l-full group`}
        >
          <div className="h-8 w-12 relative">
            <Image
              src="/Screenshot_2023-05-21_at_8.45.24_PM-removebg-preview.png"
              fill
              alt=""
              className="invert"
            />
          </div>
        </button>

        {/* P vs P */}

        <button
          type="button"
          onClick={() => handleChange("PvP")}
          className={`${
            gameType === "PvP"
              ? "bg-gray-50 dark:bg-gray-800"
              : "hover:bg-gray-50 dark:hover:bg-gray-800"
          } inline-flex flex-col items-center justify-center px-5 group`}
        >
          <div className="h-8 w-12 relative">
            <Image
              src="/Screenshot_2023-05-21_at_8.45.19_PM-removebg-preview.png"
              fill
              alt=""
              className="invert"
            />
          </div>
        </button>

        {/* PC vs PC */}
        <button
          type="button"
          onClick={() => handleChange("PCvPC")}
          className={`${
            gameType === "PCvPC"
              ? "bg-gray-50 dark:bg-gray-800"
              : "hover:bg-gray-50 dark:hover:bg-gray-800"
          } inline-flex flex-col items-center justify-center px-5 rounded-r-full group`}
        >
          <div className="h-8 w-12 relative">
            <div className="h-8 w-12 relative">
              <Image
                src="/Screenshot_2023-05-21_at_8.45.12_PM-removebg-preview.png"
                fill
                alt=""
                className="invert"
              />
            </div>
          </div>
        </button>
      </div>
    </div>
  );
}

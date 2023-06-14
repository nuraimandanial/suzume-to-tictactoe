"use client";

import { useRouter } from "next/navigation";

export default function NavBar() {
  const token = window.localStorage.getItem("token");
  const router = useRouter();
  return (
    <div className="backdrop-blur-md w-screen h-[5rem] fixed top-0 z-[101]">
      <div className="absolute left-10 top-5 flex gap-10">
        <button
          onClick={() => {
            router.push("/home");
          }}
          className="p-[0.5rem_1.5rem] font-bold text-white rounded-xl border-2 border-white backdrop-blur-xl w-[12rem]"
        >
          Home
        </button>
        <button
          onClick={() => {
            router.push("/leaderboard");
          }}
          className="p-[0.5rem_1.5rem] font-bold text-white rounded-xl border-2 border-white backdrop-blur-xl w-[12rem]"
        >
          Leader Board
        </button>
      </div>

      {token ? (
        <div className="absolute right-10 top-5 flex gap-10 items-center">
          <h1 className="text-white">{`Welcome ${window.localStorage.getItem(
            "username"
          )}!`}</h1>
          <button
            onClick={() => {
              window.localStorage.removeItem("token");
              window.localStorage.removeItem("email");
              window.localStorage.removeItem("username");
              router.push("/login");
            }}
            className="p-[0.5rem_1.5rem] w-[12rem] font-bold text-white rounded-xl border-2 border-white backdrop-blur-xl"
          >
            Log out{" "}
          </button>
        </div>
      ) : (
        ""
      )}
    </div>
  );
}

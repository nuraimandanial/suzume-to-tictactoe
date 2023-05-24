"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { useCookies } from "react-cookie";

export default function LoginPage() {
  const router = useRouter();
  const [loginCredential, setCredential] = useState({
    username: "",
    password: "",
  });
  const [cookies, setCookie, removeCookie] = useCookies(["myCookie"]);

  function handleChange(e: any) {
    const { name, value } = e.target;
    setCredential((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e: any) {
    e.preventDefault();

    if (loginCredential.username === "" || loginCredential.password === "") {
      window.alert("Please Fill in the Sign Up Form Completely!");
    } else {
      const res = await fetch("http://localhost:8080/database/login", {
        method: "POST",
        body: JSON.stringify({
          username: loginCredential.username,
          password: loginCredential.password,
        }),
        headers: {
          "Content-Type": "application/json",
        },
      });
      const message = await res.json();
      console.log(message);

      if (message.message === "Successfully Login!") {
        window.alert(message.message);

        const response = await fetch("/api/loginApi", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ username: loginCredential.username }),
        });

        const res = await response.json();
        const token = res.token;

        const get = await fetch("http://localhost:8080/database/findemail", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username: loginCredential.username }),
        });
        console.log(get);
        const getString = await get.json();
        const email = getString.email;

        window.localStorage.setItem("token", token);
        window.localStorage.setItem("username", loginCredential.username);
        window.localStorage.setItem("email", email);

        router.push("/home");
      } else if (message.message === "Incorrect Password!") {
        window.alert(message.message);
        setCredential((prev) => ({
          ...prev,
          password: "",
        }));
      } else {
        window.alert(message.message);
        setCredential({
          username: "",
          password: "",
        });
      }
    }
  }

  return (
    <div className="bg-transparent h-screen w-full flex justify-center items-center">
      <div className="z-[99] h-full w-full bg-transparent flex justify-center items-center">
        <form
          onSubmit={handleSubmit}
          className="h-[50%] w-[70%] bg-emerald-400 p-10 flex flex-col justify-evenly items-center rounded-xl shadow-xl"
        >
          <h1 className="text-2xl font-bold">Login</h1>
          <div className="flex flex-col gap-4">
            <div className="flex gap-6 items-center">
              <h1 className="w-[6rem] font-bold">Username : </h1>
              <input
                className="rounded-xl p-[0.4rem_0.8rem] shadow-xl"
                placeholder="Username"
                type="text"
                name="username"
                onChange={handleChange}
                value={loginCredential.username}
              />
            </div>
            <div className="flex gap-6 items-center">
              <h1 className="w-[6rem] font-bold">Password : </h1>
              <input
                className="rounded-xl p-[0.4rem_0.8rem] shadow-xl"
                placeholder="Password"
                type="password"
                name="password"
                onChange={handleChange}
                value={loginCredential.password}
              />
            </div>
          </div>

          <button
            type="submit"
            className="p-[0.5rem_1.5rem] bg-purple-400 rounded-xl"
          >
            Login
          </button>
        </form>
      </div>
    </div>
  );
}

"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import Swal from "sweetalert2";

export default function SignUpPage() {
  const router = useRouter();
  const [registerCredential, setCredential] = useState({
    email: "",
    userName: "",
    password: "",
  });

  function handleChange(e: any) {
    const { name, value } = e.target;
    setCredential((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e: any) {
    e.preventDefault();
    if (
      registerCredential.email === "" ||
      registerCredential.userName === "" ||
      registerCredential.password === ""
    ) {
      Swal.fire({
        title: "Error!",
        text: "Please Fill in the Sign Up Form Completely!",
        icon: "warning",
      });
    } else {
      const res = await fetch("http://localhost:8080/database/register", {
        method: "POST",
        body: JSON.stringify({
          email: registerCredential.email,
          userName: registerCredential.userName,
          password: registerCredential.password,
        }),
        headers: {
          "Content-Type": "application/json",
        },
      });
      const message = await res.json();

      if (message.message === "Successfully registered!") {
        Swal.fire({ title: message.message, icon: "success" }).then(
          async (result) => {
            if (result.isConfirmed) {
              const response = await fetch("/api/loginApi", {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({ username: registerCredential.userName }),
              });

              const res = await response.json();
              const token = res.token;
              window.localStorage.setItem("token", token);
              window.localStorage.setItem("email", registerCredential.email);
              window.localStorage.setItem(
                "username",
                registerCredential.userName
              );
              router.push("/home");
            }
          }
        );
      } else {
        Swal.fire({ title: message.message, icon: "error" });
        setCredential({
          email: "",
          userName: "",
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
          className="h-[60%] w-[70%] bg-emerald-400 p-10 flex flex-col justify-evenly items-center rounded-xl shadow-xl"
        >
          <h1 className="text-2xl font-bold">Sign Up</h1>
          <div className="flex flex-col gap-6 items-center">
            <div className="flex gap-6 items-center">
              <h1 className="w-[6rem] font-bold">Username : </h1>
              <input
                className="rounded-xl p-[0.4rem_0.8rem] shadow-xl"
                placeholder="Username"
                type="text"
                name="userName"
                onChange={handleChange}
                value={registerCredential.userName}
              />
            </div>
            <div className="flex gap-6 items-center">
              <h1 className="w-[6rem] font-bold">Email : </h1>
              <input
                className="rounded-xl p-[0.4rem_0.8rem] shadow-xl"
                placeholder="Email"
                type="email"
                name="email"
                onChange={handleChange}
                value={registerCredential.email}
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
                value={registerCredential.password}
              />
            </div>
          </div>
          <button
            type="submit"
            className="p-[0.5rem_1.5rem] bg-purple-400 rounded-xl"
          >
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
}

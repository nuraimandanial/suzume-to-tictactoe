import jwt from "jsonwebtoken";
import { NextResponse } from "next/server";

export async function POST(request: Request) {
  const secret = process.env.NEXT_PUBLIC_MY_SECRET_KEY;
  const response = await request.json();

  const token = jwt.sign({ username: response.username }, secret, {
    expiresIn: "10s",
  });

  return NextResponse.json({ token: token });
}

import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

// This function can be marked `async` if using `await` inside
export function middleware(request: NextRequest) {
  let cookie = request.cookies.get("token")?.value;
  if (cookie) {
    return NextResponse.redirect(new URL("/tictactoe", request.url));
  }
}

// See "Matching Paths" below to learn more
export const config = {
  matcher: "/tictactoe",
};

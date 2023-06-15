import PlayMusic from "./(components)/PlayMusic";
import "./globals.css";
import { Poppins } from "next/font/google";

const popins = Poppins({
  weight: ["100", "200", "300", "400", "500", "600", "700", "800", "900"],
  subsets: ["latin"],
});

export const metadata = {
  title: "Suzume's Tic Tac Toe",
  description: "Start Your Journey Here!",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className={popins.className}>
        {children}
        <audio id="audioClass" controls hidden loop>
          <source src="/onlymp3.to - Suzume no Tojimari  Theme Song  Trailer song-m3w1mUXtCj0-256k-1657457948088.mp3" />
        </audio>
        <div className="w-full flex justify-center items-center">
          <PlayMusic />
        </div>
      </body>
    </html>
  );
}

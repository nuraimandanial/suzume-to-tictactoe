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
      <body className={popins.className}>{children}</body>
    </html>
  );
}

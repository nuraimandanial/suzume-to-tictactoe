export default function ScrollButton({ boardID }: { boardID: string }) {
  function scrollToPage(number: number) {
    const element = document.getElementById(boardID);
    if (element) {
      element.scrollLeft += 600 * number;
    }
  }
  return (
    <>
      <button
        onClick={() => scrollToPage(-1)}
        className={`absolute top-1/2 bottom-1/2 left-10 -translate-y-1/2 z-[99] h-16 w-16 rounded-full bg-black text-white text-5xl grid place-items-center`}
      >
        <p>{"<"}</p>
      </button>

      <button
        onClick={() => scrollToPage(1)}
        className={`absolute top-1/2 bottom-1/2 right-10 -translate-y-1/2 z-[99] h-16 w-16 rounded-full bg-black text-white text-5xl grid place-items-center`}
      >
        <p>{">"}</p>
      </button>
    </>
  );
}

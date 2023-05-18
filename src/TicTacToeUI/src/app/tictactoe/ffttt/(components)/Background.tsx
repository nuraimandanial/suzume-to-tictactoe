export default function Background() {
  return (
    <>
      <div className="absolute h-screen w-full bg-black opacity-50 z-[2]"></div>
      <video
        className="absolute bg-transparent h-full w-full object-fill"
        autoPlay
        muted
        loop
      >
        <source src="/My Movie 2.mp4" type="video/mp4" />
      </video>
    </>
  );
}

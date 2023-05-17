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
        <source
          src="/Y2Mate.is - Live Wallpaper 4K Suzume (Makoto Shinkai)-Wd3IyXMyJdg-1080p-1659364133288.mp4"
          type="video/mp4"
        />
      </video>
    </>
  );
}

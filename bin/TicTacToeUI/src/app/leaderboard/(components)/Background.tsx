export default function Background() {
  return (
    <>
      <video
        className="fixed bg-transparent h-screen w-full object-fill"
        autoPlay
        muted
        loop
      >
        <source
          src="/Y2Mate.is - Suzume no Tojimari  Anime wallpaper-Fn828XbvOog-1080p-1659428958319.mp4"
          type="video/mp4"
        />
      </video>
      <div className="fixed h-screen w-full bg-black opacity-70"></div>
    </>
  );
}

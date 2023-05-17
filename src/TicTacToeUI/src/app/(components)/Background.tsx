export default function Background() {
  return (
    <div>
      <video
        className="fixed bg-transparent h-screen w-full object-fill"
        autoPlay
        muted
        loop
      >
        <source
          src="/y2mate.com - Suzume no Tojimari  EditAMV 4k_1080pFHR.mp4"
          type="video/mp4"
        />
      </video>
      <div className="fixed h-screen w-full bg-black opacity-70"></div>
    </div>
  );
}

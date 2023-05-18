export default class ExtractLeaderBoardClass {
  constructor() {}

  async ExtractLeaderBoard(game: string, difficulty: string) {
    try {
      const res = await fetch("http://localhost:8080/database/getleaderboard", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ game, difficulty }),
      });

      if (res.ok) {
        const data = await res.json();
        const userName = JSON.parse(
          data.userName
            .replace("[", '["')
            .replace("]", '"]')
            .replaceAll(", ", '", "')
        );
        const winLoseRatio = JSON.parse(data.winLoseRatio);
        return { userName, winLoseRatio };
      }
    } catch (err) {
      console.log(err);
    }
  }
}

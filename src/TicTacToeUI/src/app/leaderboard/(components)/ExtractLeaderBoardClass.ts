export default class ExtractLeaderBoardClass {
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
        const score = JSON.parse(data.score);

        const win = JSON.parse(data.win);
        const lose = JSON.parse(data.lose);
        return { userName, score, win, lose };
      }
    } catch (err) {
      console.log(err);
    }
  }
}

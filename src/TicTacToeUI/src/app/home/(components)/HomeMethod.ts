export default class HomeMethod {
  async ExtractLoadedBoard(userEmail: string, game: string) {
    try {
      const res = await fetch("http://localhost:8080/database/loadgame", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userEmail, game }),
      });

      const data = await res.json();
      const step1 = data.board.replace(/(X|-|O)/g, '"$1"');
      const step2 = step1.replace(/\[/g, "[").replace(/\]/g, "]");
      const output = step2.replace(/"/g, '"');
      return {
        board: JSON.parse(output),
        id: JSON.parse(data.id),
        difficulty: JSON.parse(
          data.difficulty
            .replace("[", '["')
            .replace("]", '"]')
            .replaceAll(", ", '", "')
        ),
        game: JSON.parse(
          data.game
            .replace("[", '["')
            .replace("]", '"]')
            .replaceAll(", ", '", "')
        ),
        name: JSON.parse(
          data.name
            .replace("[", '["')
            .replace("]", '"]')
            .replaceAll(", ", '", "')
        ),
      };
    } catch (err) {
      console.log(err);
    }
  }

  async DeleteSavedGame(id: number) {
    try {
      const res = await fetch("http://localhost:8080/database/delete", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ id }),
      });
      const data = await res.json();
      return data.message;
    } catch (err) {
      console.log(err);
    }
  }
}

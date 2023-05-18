export default class FetchingClass {
  async FetchPlayerMove(
    whichRow: number,
    whichCol: number,
    email: string,
    difficulty: String,
    game: string
  ) {
    await fetch(`http://localhost:8080${game}/playerMove`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        whichRow,
        whichCol,
        email,
        difficulty: difficulty,
      }),
    });
  }

  async FetchDifficulty(difficulty: string, game: string) {
    return await fetch(`http://localhost:8080${game}/difficulty`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ difficulty }),
    });
  }

  async FetchSaveGame(email: string, game: string) {
    await fetch(`http://localhost:8080${game}/savegame`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email }),
    });
  }
}

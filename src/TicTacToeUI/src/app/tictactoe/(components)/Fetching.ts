export default class FetchingClass {
  async FetchPlayerMove(
    whichRow: number,
    whichCol: number,
    email: string,
    difficulty: String,
    game: string
  ) {
    try {
      const res = await fetch(
        `http://localhost:8080${game}/${email}/playerMove`,
        {
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
        }
      );

      if (res.ok) {
        const data = await res.json();
        return data.status;
      }
    } catch (err) {
      console.log(err);
    }
  }

  async FetchPlayer2Move(
    whichRow: number,
    whichCol: number,
    email: string,
    difficulty: String,
    game: string,
    symbol: string
  ) {
    try {
      const res = await fetch(`http://localhost:8080${game}/${email}/PVPMove`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          whichRow,
          whichCol,
          email,
          difficulty: difficulty,
          symbol,
        }),
      });

      if (res.ok) {
        const data = await res.json();
        return data.status;
      }
    } catch (err) {
      console.log(err);
    }
  }

  async FetchDifficulty(difficulty: string, game: string, email: string) {
    return await fetch(`http://localhost:8080${game}/${email}/difficulty`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ difficulty }),
    });
  }

  async FetchSaveGame(email: string, game: string) {
    await fetch(`http://localhost:8080${game}/${email}/savegame`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email }),
    });
  }
}

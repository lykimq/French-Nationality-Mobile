#!/usr/bin/env python3
"""Remove near-duplicate questions from french_questions.db."""

import shutil
import sqlite3
from pathlib import Path

DB_PATH = Path(__file__).resolve().parents[1] / "app/src/main/assets/database/french_questions.db"
PROJECT_ROOT = Path(__file__).resolve().parents[1]

# Canonical question kept for the "14 juillet / fete nationale" topic.
JULY_14_CANONICAL = "reperes_historiques_0039"

JULY_14_REMOVE = [
    "valeurs_republicaines_0062",
    "devise_symboles_0011",
    "devise_symboles_0016",
    "democratie_vote_0284",
    "reperes_historiques_0147",
    "reperes_historiques_0231",
    "reperes_historiques_0304",
    "principes_et_valeurs_general_0031",
    "devise_symboles_0010",
    "principes_et_valeurs_general_0032",
]

# Bastille role: keep reperes_historiques_0185.
BASTILLE_ROLE_REMOVE = [
    "france_europe_monde_0044",
]

# Shared explanations: keep first id, remove the rest.
SHARED_EXPLANATION_REMOVE = [
  # "18 ans." -> keep democratie_vote_0060
    "democratie_vote_0183",
    "democratie_vote_0264",
    "democratie_vote_0347",
  # "27 Etats membres." -> keep france_europe_monde_0002
    "democratie_vote_0341",
  # "6 ans." -> keep democratie_vote_0158
    "vivre_en_france_general_0057",
  # IVG Constitution 2024 -> keep droits_et_devoirs_general_0056
    "droits_fondamentaux_0089",
  # 8 mai -> keep reperes_historiques_0067
    "reperes_historiques_0312",
]

REMOVE_IDS = sorted(
    set(JULY_14_REMOVE + BASTILLE_ROLE_REMOVE + SHARED_EXPLANATION_REMOVE)
)


def renumber_sort_order(conn: sqlite3.Connection) -> None:
    sub_categories = conn.execute(
        "SELECT DISTINCT sub_category_id FROM questions ORDER BY sub_category_id"
    ).fetchall()
    for (sub_category_id,) in sub_categories:
        rows = conn.execute(
            """
            SELECT id FROM questions
            WHERE sub_category_id = ?
            ORDER BY sort_order ASC, id ASC
            """,
            (sub_category_id,),
        ).fetchall()
        for index, (question_id,) in enumerate(rows):
            conn.execute(
                "UPDATE questions SET sort_order = ? WHERE id = ?",
                (index, question_id),
            )


def main() -> None:
    backup_path = PROJECT_ROOT / "french_questions.db.bak"
    shutil.copy2(DB_PATH, backup_path)
    print(f"Backup: {backup_path}")

    before = sqlite3.connect(DB_PATH)
    count_before = before.execute("SELECT COUNT(*) FROM questions").fetchone()[0]
    before.close()

    conn = sqlite3.connect(DB_PATH)
    try:
        placeholders = ",".join("?" for _ in REMOVE_IDS)
        existing = {
            row[0]
            for row in conn.execute(
                f"SELECT id FROM questions WHERE id IN ({placeholders})",
                REMOVE_IDS,
            ).fetchall()
        }
        missing = set(REMOVE_IDS) - existing
        if missing:
            raise RuntimeError(f"Expected ids not found: {sorted(missing)}")

        kept = conn.execute(
            "SELECT id FROM questions WHERE id = ?",
            (JULY_14_CANONICAL,),
        ).fetchone()
        if not kept:
            raise RuntimeError(f"Canonical id missing: {JULY_14_CANONICAL}")

        conn.executemany(
            "DELETE FROM questions WHERE id = ?",
            [(question_id,) for question_id in REMOVE_IDS],
        )
        renumber_sort_order(conn)
        conn.commit()

        count_after = conn.execute("SELECT COUNT(*) FROM questions").fetchone()[0]
        removed = count_before - count_after
        print(f"Removed {removed} questions ({count_before} -> {count_after})")
        print(f"Canonical 14 juillet: {JULY_14_CANONICAL}")
    finally:
        conn.close()


if __name__ == "__main__":
    main()

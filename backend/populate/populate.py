import psycopg2
import os

# Set your database connection parameters
DB_HOST = os.getenv('DB_HOST')
DB_NAME = os.getenv('DB_NAME')
DB_USER = os.getenv('DB_USER')
DB_PASSWORD = os.getenv('DB_PASSWORD')
DB_PORT = int(os.getenv('DB_PORT', 5432))

# set music file paths locally
MUSIC_PATH1 =  os.getenv("MUSIC_PATH1")
MUSIC_PATH2 = os.getenv("MUSIC_PATH2")
MUSIC_PATH3 = os.getenv("MUSIC_PATH3")


profile_picture_path = "/var/lib/postgres/data/_temporary_files_folder/dick_von_assman.jpg"
cover_path = "/var/lib/postgres/data/_temporary_files_folder/cover.jpg"
song_1_path = "/var/lib/postgres/data/_temporary_files_folder/wtf.mp3"
song_2_path = "/var/lib/postgres/data/_temporary_files_folder/bong.mp3"
song_3_path = "/var/lib/postgres/data/_temporary_files_folder/terezanya.mp3"

# Function to fetch the user ID by email
def get_user_id(cursor, email):
    cursor.execute("SELECT id FROM user_table WHERE email = %s", (email,))


# Utility function to fetch IDs dynamically
def get_id(cursor, table, column, value):
    cursor.execute(f"SELECT id FROM {table} WHERE {column} = %s", (value,))

    result = cursor.fetchone()
    return result[0] if result else None

try:
    connection = psycopg2.connect(
        host=DB_HOST,
        dbname=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD,
        port=DB_PORT
    )
    cursor = connection.cursor()

    # Insert Users
    cursor.execute("""
    INSERT INTO user_table (name, email, password,profile_picture , bio) VALUES
    ('Dr. Assman', 'dr.assman@example.com', 'securepassword123',pg_read_binary_file(%s), 'A passionate developer building the future of music platforms.'),
    ('John Doe', 'john.doe@example.com', 'password456',pg_read_binary_file(%s), 'Music lover and part-time drummer.'),
    ('Jane Smith', 'jane.smith@example.com', 'password789',pg_read_binary_file(%s), 'Aspiring singer and songwriter.')
    ON CONFLICT (email) DO NOTHING;
    """, (
        profile_picture_path,
        profile_picture_path,
        profile_picture_path,
    ))

    user_ids = {
        "Dr. Assman": get_id(cursor, 'user_table', 'email', 'dr.assman@example.com'),
        "John Doe": get_id(cursor, 'user_table', 'email', 'john.doe@example.com'),
        "Jane Smith": get_id(cursor, 'user_table', 'email', 'jane.smith@example.com')
    }

    # Insert Roles

    cursor.execute("""
    INSERT INTO user_roles (user_id, role) VALUES
    (%s, 'ROLE_USER'),
    (%s, 'ROLE_USER'),
    (%s, 'ROLE_USER')
    """, (
        user_ids["Dr. Assman"],
        user_ids["John Doe"],
        user_ids["Jane Smith"]
    ))

    # Insert Tags
    cursor.execute("""
    INSERT INTO tag (name) VALUES
    ('rock'), ('alternative'), ('hip-hop'), ('rap'), ('acoustic'), ('folk');
    """)

    tag_ids = {
        "rock": get_id(cursor, 'tag', 'name', 'rock'),
        "alternative": get_id(cursor, 'tag', 'name', 'alternative'),
        "hip-hop": get_id(cursor, 'tag', 'name', 'hip-hop'),
        "rap": get_id(cursor, 'tag', 'name', 'rap'),
        "acoustic": get_id(cursor, 'tag', 'name', 'acoustic'),
        "folk": get_id(cursor, 'tag', 'name', 'folk')
    }

    # Insert Songs
    cursor.execute("""
    INSERT INTO song (title, author_id,audio, cover, length) VALUES
    ('Song One', %s, pg_read_binary_file(%s),pg_read_binary_file(%s), 210),
    ('Song Two', %s, pg_read_binary_file(%s),pg_read_binary_file(%s), 180),
    ('Song Three', %s, pg_read_binary_file(%s),pg_read_binary_file(%s), 150);
    """, (
        user_ids["Dr. Assman"], song_1_path, cover_path,
        user_ids["John Doe"], song_2_path, cover_path,
        user_ids["Jane Smith"], song_3_path, cover_path
    ))

    song_ids = {
        "Song One": get_id(cursor, 'song', 'title', 'Song One'),
        "Song Two": get_id(cursor, 'song', 'title', 'Song Two'),
        "Song Three": get_id(cursor, 'song', 'title', 'Song Three')
    }

    # Insert Song Tags
    cursor.execute("""
    INSERT INTO song_tags (song_id, tag_id) VALUES
    (%s, %s), (%s, %s), (%s, %s),
    (%s, %s), (%s, %s), (%s, %s);
    """, (
        song_ids["Song One"], tag_ids["rock"],
        song_ids["Song One"], tag_ids["alternative"],
        song_ids["Song Two"], tag_ids["hip-hop"],
        song_ids["Song Two"], tag_ids["rap"],
        song_ids["Song Three"], tag_ids["acoustic"],
        song_ids["Song Three"], tag_ids["folk"]
    ))

    # Insert Playlists
    cursor.execute("""
    INSERT INTO playlist (title, user_id) VALUES
    ('My Favorite Songs', %s),
    ('Chill Vibes', %s);
    """, (
        user_ids["Dr. Assman"],
        user_ids["John Doe"]
    ))

    playlist_ids = {
        "My Favorite Songs": get_id(cursor, 'playlist', 'title', 'My Favorite Songs'),
        "Chill Vibes": get_id(cursor, 'playlist', 'title', 'Chill Vibes')
    }

    # Insert Playlist Songs
    cursor.execute("""
    INSERT INTO playlist_songs (playlist_id, song_id) VALUES
    (%s, %s), (%s, %s), (%s, %s);
    """, (
        playlist_ids["My Favorite Songs"], song_ids["Song One"],
        playlist_ids["My Favorite Songs"], song_ids["Song Two"],
        playlist_ids["Chill Vibes"], song_ids["Song Three"]
    ))

    # Insert Crews
    cursor.execute("""
    INSERT INTO crew (name) VALUES
    ('The Rockstars'), 
    ('The Acoustic Band');
    """)

    crew_ids = {
        "The Rockstars": get_id(cursor, 'crew', 'name', 'The Rockstars'),
        "The Acoustic Band": get_id(cursor, 'crew', 'name', 'The Acoustic Band')
    }

    # Insert Albums
    cursor.execute("""
    INSERT INTO album (title, crew_id, releaseYear) VALUES
    ('First Album', %s, 2023);
    """, (
        crew_ids["The Acoustic Band"],
    ))

    album_ids = {
        "First Album": get_id(cursor, 'album', 'title', 'First Album')
    }

    # Insert Album Songs
    cursor.execute("""
    INSERT INTO album_songs (album_id, song_id) VALUES
    (%s, %s), (%s, %s);
    """, (
        album_ids["First Album"], song_ids["Song One"],
        album_ids["First Album"], song_ids["Song Three"]
    ))

    # Insert Crew Members
    cursor.execute("""
    INSERT INTO crew_members (crew_id, user_id) VALUES
    (%s, %s), (%s, %s), (%s, %s);
    """, (
        crew_ids["The Rockstars"], user_ids["Dr. Assman"],
        crew_ids["The Rockstars"], user_ids["John Doe"],
        crew_ids["The Acoustic Band"], user_ids["Jane Smith"]
    ))

    # Insert Following Relationships
    cursor.execute("""
    INSERT INTO following_table (followed_user_id, follower_user_id) VALUES
    (%s, %s), (%s, %s), (%s, %s);
    """, (
        user_ids["Dr. Assman"], user_ids["John Doe"],
        user_ids["John Doe"], user_ids["Jane Smith"],
        user_ids["Jane Smith"], user_ids["Dr. Assman"]
    ))

    # Insert Comments
    cursor.execute("""
    INSERT INTO comments (song_id, user_id, text) VALUES
    (%s, %s, 'This song rocks!'),
    (%s, %s, 'Nice beat!');
    """, (
        song_ids["Song One"], user_ids["John Doe"],
        song_ids["Song Two"], user_ids["Dr. Assman"]
    ))

    # Commit the transaction
    connection.commit()
    print("Data inserted successfully!")

except Exception as e:
    print(f"An error occurred: {e}")
    connection.rollback()

finally:
    if cursor:
        cursor.close()
    if connection:
        connection.close()

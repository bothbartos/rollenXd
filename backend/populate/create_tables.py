import psycopg2
import os
# from dotenv import load_dotenv

#load them env vars, yeah!
# load_dotenv()

# Set your database connection parameters

DB_HOST = os.getenv('DB_HOST')
DB_NAME = os.getenv('DB_NAME')
DB_USER = os.getenv('DB_USER')
DB_PASSWORD = os.getenv('DB_PASSWORD')
DB_PORT = int(os.getenv('DB_PORT', 5432))


# Create a connection to the database
try:
    connection = psycopg2.connect(
        host=DB_HOST,
        dbname=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD,
        port=DB_PORT
    )


    # Create a cursor object to execute SQL commands
    cursor = connection.cursor()

    # SQL script for creating tables
    create_tables_sql = """
        ALTER TABLE IF EXISTS Following_Table DROP CONSTRAINT IF EXISTS following_table_followed_user_id_fkey;
        ALTER TABLE IF EXISTS Following_Table DROP CONSTRAINT IF EXISTS following_table_follower_user_id_fkey;
        ALTER TABLE IF EXISTS Crew_Members DROP CONSTRAINT IF EXISTS crew_members_crew_id_fkey;
        ALTER TABLE IF EXISTS Crew_Members DROP CONSTRAINT IF EXISTS crew_members_user_id_fkey;
        ALTER TABLE IF EXISTS Song_Tags DROP CONSTRAINT IF EXISTS song_tags_song_id_fkey;
        ALTER TABLE IF EXISTS Song_Tags DROP CONSTRAINT IF EXISTS song_tags_tag_id_fkey;
        ALTER TABLE IF EXISTS Playlist_Songs DROP CONSTRAINT IF EXISTS playlist_songs_playlist_id_fkey;
        ALTER TABLE IF EXISTS Playlist_Songs DROP CONSTRAINT IF EXISTS playlist_songs_song_id_fkey;
        ALTER TABLE IF EXISTS Album_Songs DROP CONSTRAINT IF EXISTS album_songs_album_id_fkey;
        ALTER TABLE IF EXISTS Album_Songs DROP CONSTRAINT IF EXISTS album_songs_song_id_fkey;
        ALTER TABLE IF EXISTS Comments DROP CONSTRAINT IF EXISTS comments_song_id_fkey;
        ALTER TABLE IF EXISTS Comments DROP CONSTRAINT IF EXISTS comments_user_id_fkey;

        DROP TABLE IF EXISTS Following_Table;
        DROP TABLE IF EXISTS Crew_Members;
        DROP TABLE IF EXISTS Song_Tags;
        DROP TABLE IF EXISTS Playlist_Songs;
        DROP TABLE IF EXISTS Album_Songs;
        DROP TABLE IF EXISTS Comments;
        DROP TABLE IF EXISTS Playlist;
        DROP TABLE IF EXISTS Album;
        DROP TABLE IF EXISTS Song;
        DROP TABLE IF EXISTS Tag;
        DROP TABLE IF EXISTS Crew;
        DROP TABLE IF EXISTS User_Table;


    -- USERS Table
    CREATE TABLE User_Table (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        profile_picture BYTEA,
        bio TEXT
    );
    
    CREATE TABLE Following_Table (
        followed_user_id INT NOT NULL,
        follower_user_id INT NOT NULL,
        FOREIGN KEY (followed_user_id) REFERENCES User_Table(id) ON DELETE CASCADE,
        FOREIGN KEY (follower_user_id) REFERENCES User_Table(id) ON DELETE CASCADE
    );
    
    -- CREW Table
    CREATE TABLE Crew (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL
    );
    
    CREATE TABLE Crew_Members(
        crew_id INT NOT NULL,
        user_id INT NOT NULL,
        FOREIGN KEY (crew_id) REFERENCES Crew(id) ON DELETE CASCADE,
        FOREIGN KEY (user_id) REFERENCES User_Table(id) ON DELETE CASCADE 
    );

    -- SONGS Table
    CREATE TABLE Song (
        id SERIAL PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        author_id INT NOT NULL, -- References Users table
        audio BYTEA, -- Binary data for storing audio
        cover BYTEA,
        length DOUBLE PRECISION, -- Length of song in seconds
        numberOfLikes INT DEFAULT 0,
        re_share INT DEFAULT 0,
        FOREIGN KEY (author_id) REFERENCES User_Table(id) ON DELETE CASCADE
    );
    
    -- TAGS Table
    CREATE TABLE Tag (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL UNIQUE
    );
    
    CREATE TABLE Song_Tags(
        song_id INT NOT NULL,
        tag_id INT NOT NULL,
        FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE,
        FOREIGN KEY (tag_id) REFERENCES Tag(id) ON DELETE CASCADE 
    );

    -- PLAYLISTS Table
    CREATE TABLE Playlist (
        id SERIAL PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        user_id INT NOT NULL, -- References Users table
        FOREIGN KEY (user_id) REFERENCES User_Table(id) ON DELETE CASCADE
    );
    
    CREATE TABLE Playlist_Songs (
        playlist_id INT NOT NULL,
        song_id INT NOT NULL,
        FOREIGN KEY (playlist_id) REFERENCES Playlist(id) ON DELETE CASCADE,
        FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE 
    );

    -- ALBUMS Table
    CREATE TABLE Album (
        id SERIAL PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        crew_id INT NOT NULL, -- References Users table
        releaseYear INT,
        FOREIGN KEY (crew_id) REFERENCES Crew(id) ON DELETE CASCADE
    );
    
    CREATE TABLE Album_Songs (
        album_id INT NOT NULL,
        song_id INT NOT NULL,
        FOREIGN KEY (album_id) REFERENCES Album(id) ON DELETE CASCADE,
        FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE
    );

    -- COMMENTS Table
    CREATE TABLE Comments (
        id SERIAL PRIMARY KEY,
        song_id INT NOT NULL, -- References Songs table
        user_id INT NOT NULL, -- References Users table
        text TEXT NOT NULL,
        FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE,
        FOREIGN KEY (user_id) REFERENCES User_Table(id) ON DELETE CASCADE
    );
    """

    # Execute the SQL to create the tables
    cursor.execute(create_tables_sql)

    # Commit the transaction
    connection.commit()
    print("Tables created successfully!")

except Exception as e:
    print(f"An error occurred: {e}")
    connection.rollback()  # Rollback in case of error

finally:
    # Close the cursor and connection
    if cursor:
        cursor.close()
    if connection:
        connection.close()

export function convertDoubleToMinuteSecond(seconds) {
    let minutes = Math.floor(seconds / 60);
    let newSeconds = Math.floor(seconds - minutes * 60);

    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (newSeconds < 10) {
        newSeconds = "0" + newSeconds;
    }

    return `${minutes}:${newSeconds}`;
}

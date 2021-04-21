# Challenge answer

The following is an answer to the [Challenge](./CHALLENGE.md) for the Web Developer position specifically
on the Payload-Planner project.

## Clock ins

Just a listing of the times spent on this answer:

| Start            | End   |  Duration  | Comments                                             |
| :--------------- | :---- | :--------: | :--------------------------------------------------- |
| 2021-04-14 17:25 | 18:20 |  55 mins   | Setup and planning                                   |
| 2021-04-20 15:51 | 16:47 |  56 mins   | boiler plate and maven issues                        |
| 2021-04-20 17:24 | 22:02 | 4h 39 mins | actual start to coding, with alot of setup necessary |

## Technologies used

The language used to code the answer is [Kotlin](https://kotlinlang.org/) mainly because I like it's type system and
its strong functional capabilities.


## Development Process

See my notes [Notes](Notes.md) for more information on the process taken.


## Running

In order to run this application, it has been developed and tested in intellij IDEA.
For best results use that.

In order to run the application easily, open the file `app/src/Application` and click the play button next to the main function.
From there, it serves a web server on port 8080. There is a listenner for post requests on the root route `/`. It expects requests of the following format.

```json
{
    "tles": [
        "ISS (ZARYA)\n1 25544U 98067A   21110.88790192  .00002325  00000-0  50487-4 0  9999\n2 25544  51.6450 266.1357 0002502 255.7332 264.5540 15.48912929279711",

        "1 25544U 98067A   21110.88790192  .00002325  00000-0  50487-4 0  9999\n2 25544  51.6450 266.1357 0002502 255.7332 264.5540 15.48912929279711"
    ],
    "targets":[
        {
            "name": "Montreal",
            "lat": 45.50884,
            "lng": -73.58781
        }
    ]
}
```

Note that the TLE's are compressed into a single line with explicit `\n` escape characters.
This is due to the characteristics of json not being able to take multi-line strings.




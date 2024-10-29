<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://unpkg.com/in-view@latest/dist/in-view.min.js"></script>
    <style>
        @import url('https://fonts.googleapis.com/css?family=Roboto+Mono:300,500');

        html, body {
            width: 100%;
            height: 100%;
        }

        body {
            margin: 0;
            background-image: url(https://s3-us-west-2.amazonaws.com/s.cdpn.io/257418/andy-holmes-698828-unsplash.jpg);
            background-size: cover;
            background-repeat: no-repeat;
            min-height: 100vh;
            min-width: 100vw;
            font-family: "Roboto Mono", "Liberation Mono", Consolas, monospace;
            color: rgba(255,255,255,.87);
        }

        .mx-auto {
            margin-left: auto;
            margin-right: auto;
        }

        .container,
        .container > .row,
        .container > .row > div {
            height: 100%;
        }

        #countUp {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100%; /* Ensure it's full height */
        }

        .number {
            font-size: 4rem;
            font-weight: 500;
        }

        .text {
            font-weight: 300;
            text-align: center;
            margin: 0 0 1rem;
        }
        a {
            color: #f0db4f; /* Bright color for visibility */
            text-decoration: none; /* Remove underline */
            font-weight: bold; /* Make it bold */
            padding: 10px 15px; /* Add some padding */
            border: 2px solid transparent; /* Initial border for hover effect */
            border-radius: 5px; /* Rounded corners */
            transition: background-color 0.3s, border-color 0.3s; /* Smooth transition */
        }

        a:hover {
            background-color: rgba(255, 255, 255, 0.2); /* Light background on hover */
            border-color: #f0db4f; /* Change border color on hover */
            color: #ffffff; /* Change text color on hover for contrast */
        }
    </style>
    <script>
        var formatThousandsNoRounding = function(n, dp) {
            var e = '', s = e+n, l = s.length, b = n < 0 ? 1 : 0,
                i = s.lastIndexOf(','), j = i == -1 ? l : i,
                r = e, d = s.substr(j+1, dp);
            while ((j -= 3) > b) { r = '.' + s.substr(j, 3) + r; }
            return s.substr(0, j + 3) + r +
                (dp ? ',' + d + (d.length < dp ?
                    ('00000').substr(0, dp - d.length):e):e);
        };

        var hasRun = false;

        $(document).ready(function() {
            inView('#countUp').on('enter', function() {
                console.log('CountUp entered the viewport.'); // Debugging log
                if (!hasRun) {
                    $('.number').each(function() {
                        var $this = $(this),
                            countTo = $this.attr('data-count');

                        $({ countNum: $this.text() }).animate({
                                countNum: countTo
                            },
                            {
                                duration: 1000,
                                easing: 'linear',
                                step: function() {
                                    $this.text(formatThousandsNoRounding(Math.floor(this.countNum)));
                                },
                                complete: function() {
                                    $this.text(formatThousandsNoRounding(this.countNum));
                                }
                            });
                    });
                    hasRun = true;
                }
            });
        });
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="xs-12 md-6 mx-auto">
            <div id="countUp">
                <div class="number" data-count="${status}">0</div>
                <div class="text">${message}</div>
                <div class="text"><a href="/">Return To Homepage.</a></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>


<%@ page import="java.util.Arrays" %>
<%@ page import="java.text.Normalizer" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%
    String word = request.getParameter("word");
%>

<!DOCTYPE html>

<!--
// WEBSITE: https://themefisher.com
// TWITTER: https://twitter.com/themefisher
// FACEBOOK: https://www.facebook.com/themefisher
// GITHUB: https://github.com/themefisher/
-->

<html lang="es">

<head>
    <meta charset="utf-8">
    <title>Universitas</title>

    <!-- mobile responsive meta -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- theme meta -->
    <meta name="theme-name" content="godocs-bulma"/>

    <!-- ** Plugins Needed for the Project ** -->
    <!-- plugins -->
    <link rel="stylesheet" href="media/css/bulma.min.css">
    <link rel="stylesheet" href="media/css/themify-icons.css">
    <!-- Main Stylesheet -->
    <link href="media/css/style.css" rel="stylesheet">

    <!--Favicon-->
    <link rel="shortcut icon" href="media/images/favicon.ico" type="image/x-icon">
    <link rel="icon" href="imedia/mages/favicon.ico" type="image/x-icon">

</head>

<body>
<script id="__bs_script__">//<![CDATA[
(function () {
    try {
        var script = document.createElement('script');
        if ('async') {
            script.async = true;
        }
        script.src = 'media/js/browser-sync-client.js?v=2.29.3'.replace("HOST", location.hostname);
        if (document.body) {
            document.body.appendChild(script);
        } else if (document.head) {
            document.head.appendChild(script);
        }
    } catch (e) {
        console.error("Browsersync: could not append script tag", e);
    }
})()
//]]></script>

<!-- banner -->
<section class="section mt-1">
    <div class="container">
        <div class="columns is-justify-content-space-between is-align-items-center">
            <div class="column is-7-desktop has-text-centered-mobile has-text-centered-tablet has-text-left-desktop">
                <h1 class="mb-4">Palíndromo</h1>
                <p class="mb-4">Palabra o expresión que es igual si se lee de izquierda a derecha que de derecha a
                    izquierda.</p>
                <form class="search-wrapper" action="index.jsp">
                    <input id="word" name="word" type="text" class="input input-lg"
                           placeholder="Escribe aquí una palabra">
                    <button type="submit" class="btn btn-primary">Enviar</button>
                </form>
            </div>
            <div class="column is-4-desktop hidden-on-tablet">
                <img src="media/images/banner.jpg" alt="illustration" class="img-fluid" width="210px">
            </div>

        </div>
        <%
            if (word != null) {
        %>
        <div class="column is-7-desktop has-text-centered-mobile has-text-centered-tablet has-text-left-desktop mt-5">
            <h4 class="mb-5">Palabra ingresada</h4>
            <div class="table-container table is-bordered">
                <table class="table">
                    <tbody>
                    <tr>
                        <%
                            word = word.replaceAll("\\s", "");
                            word = Normalizer.normalize(word, Normalizer.Form.NFD);
                            word = word.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                            word = word.replaceAll("[^a-zA-Z0-9]", "");
                            word = word.toLowerCase();
                            String[] arrayWord = word.split("");
                            String[] arrayReverse = new String[arrayWord.length];
                            for (int i = 0; i < arrayWord.length; i++) {
                                arrayReverse[i] = arrayWord[arrayWord.length - 1 - i];
                        %>
                        <td><%= arrayWord[i] %>
                        </td>
                        <%
                            }
                        %>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="container">
            <div class="columns is-align-items-center">
                <%
                    if (Arrays.equals(arrayReverse, arrayWord)) {
                %>
                <div class="column is-4-desktop has-text-centered hidden-on-tablet">
                    <img src="media/images/si.gif" class="img-fluid" alt="" width="200px">
                </div>
                <div class="column is-8-desktop has-text-centered-mobile has-text-centered-tablet has-text-left-desktop">
                    <h2>Es un palíndromo</h2>
                </div>
                <%
                    } else {
                %>
                <div class="column is-4-desktop has-text-centered hidden-on-tablet">
                    <img src="media/images/no.gif" class="img-fluid" alt="" width="200px">
                </div>
                <div class="column is-8-desktop has-text-centered-mobile has-text-centered-tablet has-text-left-desktop">
                    <h2>No es un palíndromo</h2>
                </div>
                <%
                    }
                %>
            </div>
        </div>
        <%
            }
        %>
    </div>

</section>
<!-- /banner -->

<!-- plugins -->
<script src="media/js/jquery.min.js"></script>
<script src="media/js/masonry.min.js"></script>
<script src="media/js/clipboard.min.js"></script>
<script src="media/js/jquery.matchHeight-min.js"></script>

<!-- Main Script -->
<script src="media/js/script.js"></script>

</body>
</html>
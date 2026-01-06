<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
            background-color: #f2f4f6;
            margin: 0;
            padding: 20px;
        }
        .container {
            background-color: #ffffff;
            border-radius: 10px;
            padding: 40px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            margin: auto;
        }
        h1 {
            color: #007bff;
            font-size: 24px;
            border-bottom: 3px solid #007bff;
            padding-bottom: 10px;
        }
        p {
            color: #555;
            line-height: 1.6;
        }
        ul {
            padding-left: 5px;
            color: #333;
            list-style-type: none;
        }
        ul li {
            padding-top: 5px;
            padding-bottom: 5px;
        }
        .icon {
            width: 20px;
            vertical-align: middle;
            margin-right: 8px;
        }
        .details {
            background-color: #f8f9fa;
            border-radius: 5px;
            padding: 15px;
            margin-top: 20px;
            border-left: 5px solid #007bff;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Avis d'Approbation de Visa</h1>
    <p>Bonjour,</p>
    
    <p>Nous avons le plaisir de vous informer qu'une nouvelle demande de visa a été approuvée. Voici les détails :</p>
    
    <div class="details">
        <ul>
            <li><img src="https://img.icons8.com/ios-filled/24/007bff/checkmark.png" class="icon" alt="Check"> <strong>Nom du demandeur :</strong> ${name}</li>
            <li><img src="https://img.icons8.com/ios-filled/24/007bff/document.png" class="icon" alt="Document"> <strong>Numéro de demande :</strong> ${applicationNumber}</li>
            <li><img src="https://img.icons8.com/ios-filled/24/007bff/passport.png" class="icon" alt="Visa"> <strong>Type de visa :</strong> ${visa_type}</li>
        </ul>
    </div>
    
    <p>Merci de prendre note de cette approbation.</p>
</div>

</body>
</html>

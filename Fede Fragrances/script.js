document.addEventListener("DOMContentLoaded", () => {
  const contenitore = document.getElementById("contenitore");
  if (contenitore) {
    fetch("dati.json")
      .then(response => {
        if (!response.ok) throw new Error("Errore nel caricamento del file JSON");
        return response.json();
      })
      .then(data => {
        prodotti = data;
        mostraProdotti();
      })
      .catch(error => console.error("Errore:", error));
  }
});

let prodotti = [];

function mostraProdotti() {
  const contenitore = document.getElementById("contenitore");
  contenitore.innerHTML = "";

  prodotti.forEach(prodotto => {
    const card = document.createElement("div");
    card.className = "card";

    card.innerHTML = `
      <img src="${prodotto.immagine}" alt="${prodotto.nome}" class="immagine-prodotto">
      <h3>${prodotto.nome}</h3>
      <p>${prodotto.descrizione}</p>
      <p><strong>€${prodotto.prezzo.toFixed(2)}</strong></p>
      <button onclick="aggiungiAlCarrello(${prodotto.id})">Aggiungi al carrello</button>
    `;
    contenitore.appendChild(card);
  });
}

function aggiungiAlCarrello(idProdotto) {
  let carrello = JSON.parse(localStorage.getItem("carrello")) || [];
  const prodotto = prodotti.find(p => p.id === idProdotto);
  if (prodotto) {
    carrello.push(prodotto);
    localStorage.setItem("carrello", JSON.stringify(carrello));
    alert("Prodotto aggiunto al carrello!");
  }
}

function mostraCarrello() {
  const contenitore = document.getElementById("contenitore-carrello");
  const carrello = JSON.parse(localStorage.getItem("carrello")) || [];

  if (carrello.length === 0) {
    contenitore.innerHTML = "<p>Il carrello è vuoto.</p>";
    return;
  }

  contenitore.innerHTML = "";

  carrello.forEach(prodotto => {
    const card = document.createElement("div");
    card.className = "card";

    card.innerHTML = `
      <img src="${prodotto.immagine}" alt="${prodotto.nome}" class="immagine-prodotto">
      <h3>${prodotto.nome}</h3>
      <p>${prodotto.descrizione}</p>
      <p><strong>€${prodotto.prezzo.toFixed(2)}</strong></p>
    `;
    contenitore.appendChild(card);
  });
}

function svuotaCarrello() {
  localStorage.removeItem("carrello"); // Cancella tutto
  mostraCarrello(); // Ricarica la pagina
  alert("Carrello svuotato.");
}

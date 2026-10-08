const getRestaurantList = "https://magno.di.uevora.pt/tweb/t1/restaurante/list";
const getOffersList = "https://magno.di.uevora.pt/tweb/t1/oferta/list";
const googleMapsBaseUrl = "https://www.google.com/maps?q=";

const content = document.getElementById("content");
let xhr = new XMLHttpRequest();

let restaurantCurrentPage = 0;
let offersCurrentPage = 0;


// -------------------------------------------------- OFFERS --------------------------------------------------


/**
 * Fetch and display offers list
 */
function showOffers() {
  restaurantCurrentPage = 0; //reset restaurant page

  xhr.open("POST", getOffersList, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let data = `page=${offersCurrentPage}`;
  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const offers = JSON.parse(this.responseText);
      let output = `<div id="list-container"><h1>Ofertas</h1><ul id="offer-list">`;

      offers.oferta_set.forEach(function (offer) {
        output += renderOffersCard(offer);
      });

      let isPrevPage = "";
      if (offers.page == 0) {
        isPrevPage = "disabled";
      }
      let isNextPage = "";
      if (offers.page == offers.last_page) {
        isNextPage = "disabled";
      }
      output += "</ul>";
      output += ` <div id="list-menu">
                          <button onclick="offersPrevPage()" id="offers-prev-page" ${isPrevPage}>
                            <svg
                              xmlns="http://www.w3.org/2000/svg"
                              height="24px"
                              viewBox="0 -960 960 960"
                              width="24px"
                              fill="#000000"
                            >
                              <path d="M560-240 320-480l240-240 56 56-184 184 184 184-56 56Z" />
                            </svg>
                          </button>
                          <span id="page-info">Página <strong>${
                            offers.page + 1
                          }</strong> de ${offers.last_page + 1}</span>
                          <button onclick="offersNextPage()" id="offers-next-page" ${isNextPage}>
                            <svg
                              xmlns="http://www.w3.org/2000/svg"
                              height="24px"
                              viewBox="0 -960 960 960"
                              width="24px"
                              fill="#000000"
                            >
                              <path d="M504-480 320-664l56-56 240 240-240 240-56-56 184-184Z" />
                            </svg>
                          </button>
                        </div>
                      </div>`;
      content.innerHTML = output;
    }
  };
  xhr.send(data);
}


/**
 * Renders an offer card.
 * @param {Object} offer, that contains offer details.
 * @returns {string} HTML string for the offer card.
 */
function renderOffersCard(offer) {
  const name = offer.nome;
  const offer_id = offer.oferta_id;
  const offer_image = offer.foto;
  const offer_description = offer.descricao;
  const restaurant_id = offer.restaurante_id;
  const units = offer.unidades;

  return `
        <li class="offer-card">
          <img 
            src="${offer_image}" 
            alt="${name}" 
            class="offer-image" 
            onerror="errorLoadingImage(this);"
          />
          <div class="offer-header">
            <div class="offer-title">
              <h3>${name}</h3>
              <p class="offer-restaurant-name">Restaurante #${restaurant_id}</p>
            </div>
            <span class="offer-id">ID ${offer_id}</span>
          </div>
          <hr />
          <div class="offer-info">
            <p class="offer-description">
              ${offer_description}
            </p>
            <span class="offer-units">${units} Unidades</span>
          </div>
        </li>`;
}

/**
 * Change to the next page of offers and fetch data.
 */
function offersNextPage() {
  offersCurrentPage += 1;
  showOffers();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Change to the previous page of offers and fetch data.
 */
function offersPrevPage() {
  offersCurrentPage -= 1;
  showOffers();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


// -------------------------------------------------- RESTAURANTS --------------------------------------------------


/**
 * Fetch and display restaurant list.
 * Sends a POST request to the restaurant list endpoint and renders the received data.
 * Updates the content div with the list of restaurants and pagination controls.
 */
function showRestaurants() {
  offersCurrentPage = 0; //reset offers page

  xhr.open("POST", getRestaurantList, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let data = `page=${restaurantCurrentPage}`;

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const restaurants = JSON.parse(this.responseText);
      let output = `<div id="list-container"><h1>Restaurantes</h1><ul id="generic-card-list">`;

      restaurants.restaurante_set.forEach(function (restaurant) {
        output += renderRestaurantCard(restaurant);
      });

      let isPrevPage = "";
      if (restaurants.page == 0) {
        isPrevPage = "disabled";
      }
      let isNextPage = "";
      if (restaurants.page == restaurants.last_page) {
        isNextPage = "disabled";
      }
      output += "</ul>";
      output += ` <div id="list-menu">
                    <button onclick="restaurantPrevPage()" id="restaurants-prev-page" ${isPrevPage}>
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        height="24px"
                        viewBox="0 -960 960 960"
                        width="24px"
                        fill="#000000"
                      >
                        <path d="M560-240 320-480l240-240 56 56-184 184 184 184-56 56Z" />
                      </svg>
                    </button>
                    <span id="page-info">Página <strong>${
                      restaurants.page + 1
                    }</strong> de ${restaurants.last_page + 1}</span>
                    <button onclick="restaurantNextPage()" id="restaurants-next-page" ${isNextPage}>
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        height="24px"
                        viewBox="0 -960 960 960"
                        width="24px"
                        fill="#000000"
                      >
                        <path d="M504-480 320-664l56-56 240 240-240 240-56-56 184-184Z" />
                      </svg>
                    </button>
                  </div>
                </div>`;
      content.innerHTML = output;
    }
  };
  xhr.send(data);
}


/**
 * Renders a restaurant card.
 * @param {Object} restaurant, that contains restaurant details.
 * @returns {string} HTML string for the restaurant card.
 */
function renderRestaurantCard(restaurant) {
  const name = restaurant.nome;
  const id = restaurant.restaurante_id;
  const loc = restaurant.localizacao;
  const mapUrl = `${googleMapsBaseUrl}${loc.lat},${loc.long}`;

  return `
          <li class="generic-card">
            <div class="generic-card-header">
              <h3>${name}</h3>
              <span class="generic-id">ID ${id}</span>
            </div>
            <hr />
            <div class="restaurant-location">
              <p><strong>Morada:</strong> ${loc.morada}</p>
              <p><strong>Código Postal:</strong> ${loc.cod_postal}</p>
            </div>
            <div class="restaurant-maps">
              <a href="${mapUrl}" target="_blank">
                Ver no Google Maps
              </a>
            </div>
          </li>`;
}


/**
 * Change to the next page of restaurants and fetch data.
 */
function restaurantNextPage() {
  restaurantCurrentPage += 1;
  showRestaurants();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Change to the previous page of restaurants and fetch data.
 */
function restaurantPrevPage() {
  restaurantCurrentPage -= 1;
  showRestaurants();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


// -------------------------------------------------- UTILITIES --------------------------------------------------


/**
 * Handles image loading errors by setting a default placeholder image.
 * @param {HTMLImageElement} image 
 */
function errorLoadingImage(image) {
  image.onerror = null; // Avoiding an infinite loop if the placeholder also fails
  image.src = "../assets/img/default-offer-image-placeholder.png";
}


/**
 * On window load, check the URL hash and display the corresponding section.
 * 
 * So that this functionality can be accessed from the login page directly
 */
window.onload = function () {
  const hash = window.location.hash.substring(1); // Remove the #

  if (hash === "restaurantes") {
    showRestaurants();
  } else if (hash === "ofertas") {
    showOffers();
  }
};

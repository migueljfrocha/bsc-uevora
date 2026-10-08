/* NAVBAR FUNCTIONS */

/**
 * Shows the burgerbar and hides the burger button.
 */
function showBurgerbar() {
  const burgerbar = document.querySelector(".burgerbar");
  const burger = document.querySelector("#burger-button");
  burgerbar.style.display = "flex";
  burger.style.display = "none";
}


/**
 * Hides the burgerbar and shows the burger button.
 */
function hideBurgerbar() {
  const burgerbar = document.querySelector(".burgerbar");
  const burger = document.querySelector("#burger-button");
  burgerbar.style.display = "none";
  burger.style.display = "block";
}

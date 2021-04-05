function initializareSelectIntrebari(intrebari) {
    let element = document.getElementById("chestionarId");
    let chestionarAles = parseInt(element.options[element.selectedIndex].value);
    let selectIntrebari = document.getElementById("intrebareId");
    let lungime = selectIntrebari.options.length;
    for (let index = lungime - 1; index >= 0; index--) {
        selectIntrebari[index] = null;
    }
    for (let index = 0; index < intrebari.length; index++) {
        let intrebare = intrebari[index];
        if (intrebare.chestionarId === chestionarAles) {
            let optiuneNoua = document.createElement("option");
            optiuneNoua.text = 'ID intrebare: '.concat(intrebare.intrebareId).concat(' | ').concat(intrebare.continut);
            optiuneNoua.value = intrebare.intrebareId;
            selectIntrebari.add(optiuneNoua);
        }
    }
}
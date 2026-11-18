package com.example.albalab.model

data class Contenido(
    val clave: String,
    val titulo: String,
    val cuerpo: String
)

object ContenidoDefaults {

    val items = listOf(
        Contenido(
            clave = "nosotros_titulo",
            titulo = "Conócenos",
            cuerpo = "Conoce más sobre Alba Lab y el trabajo que realizamos."
        ),
        Contenido(
            clave = "nosotros_que_es",
            titulo = "¿Qué es Alba Lab?",
            cuerpo = "Alba Lab es una iniciativa vinculada a CORPALIV que busca promover oportunidades de inclusión social y laboral."
        ),
        Contenido(
            clave = "nosotros_mision",
            titulo = "Nuestra misión",
            cuerpo = "Promover la inclusión consciente, valorando la diversidad funcional y generando oportunidades para todas las personas."
        ),
        Contenido(
            clave = "nosotros_vision",
            titulo = "Nuestra visión",
            cuerpo = "Construir una sociedad más inclusiva, accesible y consciente, donde todas las personas puedan participar y desarrollarse."
        ),
        Contenido(
            clave = "nosotros_que_hacemos",
            titulo = "¿Qué hacemos?",
            cuerpo = ""
        ),
        Contenido(
            clave = "nosotros_inclusion",
            titulo = "Inclusión laboral",
            cuerpo = "Promovemos oportunidades que favorecen la participación e inclusión de personas con discapacidad en distintos espacios laborales."
        ),
        Contenido(
            clave = "nosotros_productos",
            titulo = "Productos",
            cuerpo = "Alba Lab desarrolla productos que reflejan el trabajo, la creatividad y las capacidades de las personas que forman parte de la organización."
        ),
        Contenido(
            clave = "nosotros_vinculacion",
            titulo = "Vinculación con empresas",
            cuerpo = "Buscamos generar vínculos y oportunidades de colaboración con empresas y organizaciones interesadas en la inclusión."
        )
    )
}

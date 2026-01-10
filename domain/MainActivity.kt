package com.example.domain

import android.os.Bundle
import android.util.Log

import androidx.compose.runtime.getValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.ui.theme.Viikkoteht1NativeTheme
import com.example.domain.Task
import com.example.domain.mockdata


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Viikkoteht1NativeTheme {
                Column()
                {
                    Homescreen()
                }

            }
        }
    }
}

    @Composable
    fun Homescreen()
    {
        var taskilista by remember { mutableStateOf(mockdata) } //TEXT UI:ssa päivittyy kun taskilista muuttuu. alustetaan arvoon mockdata.
        var toggledone by remember { mutableStateOf(false) } //koska toggledone on TEXTin sisällä, nii sekin aiheuttaa UI updaten

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "tehtävälista",style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(10.dp))
                //if(mockdata[1].done){}
                taskilista.forEach { task ->
                Text(text = "${task.title} - Due: ${task.dueDate} done?: ${task.done}") //tämä TEXT päivittyy by remembereiden mukana
                 Button(
                         onClick = {
                             taskilista = toggleDone(task.id-1)

                         },
                 )
                 {
                     Text(text = "merkkaa valmiiksi")
                 }
            }
        }
        if(!toggledone)
        {
            Button(
                onClick = {

                        val uusiTaski =
                            Task( //luodaan uus task objekti. lisätään se taskilistaan, jota by remember seuraa --> UI päivitys
                                id = mockdata.size + 1,
                                title = "Uusi taski",
                                description = "testi taski",
                                priority = 5,
                                dueDate = "millon haluat",
                                done = false
                            )
                        taskilista =
                            addTask(uusiTaski) // muutetaan taskilista addtaskin return arvoksi, eli uudeksi listaksi.
                        //addtask ottaa parametrinä vastaan uusiTaski objektin, luo uuden listan taskilistan pohjalta, lisää uusiTaski objektin siihen listaan ja palauttaa uuden listan.

                          },
            )
            {
                Text(text = "lisää taski") //buttonin teksti
            }
        }
        Button(
            onClick = {
                taskilista = showDone(toggledone) // muutetaan taskilista näyttämään vain valmiit. boolean muuttuja seuraamaan tilaa.
                if(toggledone){toggledone = false} //otetaan vastaan toggledone bool arvo funktion parametrinä, minkä perusteella funktio palauttaa joko
                else{toggledone = true} // listan, missä on vain done == true tai alkuperäisen listan.
            },
        )
        {
            if(!toggledone) {
                Text(text = "näytä valmiit") //buttonin teksti
            }
            else{
                Text(text = "näytä kaikki")
            }
        }
        if(!toggledone)
        {
        Button(
            onClick = {
                taskilista = sortByDate() // muutetaan taskilista näyttämään sortattu lista.
            },
        )
        {
            Text(text = "järjestele") //buttonin teksti
        }
        }
    }

fun showDone(toggledone: Boolean): List<Task>
{
    Log.d("toggledone", "$toggledone")
    if(!toggledone)
    {
    val mutableList = mockdata.toMutableList() //tomutablelist luo muutettavan kopion mockdatasta
    val doneList = mutableListOf<Task>()
    mutableList.forEach { task ->
        if(task.done) //luodaan uusi lista jonne hyväksytään ainoastaan ne Task objektit, missä done == true
        {
            doneList.add(task)
        }
    }
    doneList.toList()
    return doneList
    }
    else
    {
    return mockdata
    }
}
fun addTask(uusitaski: Task): List<Task>
{
    mockdata += uusitaski
    return mockdata
}
fun toggleDone(id: Int): List<Task>
{
    Log.d("bool", "${mockdata[id].id}")
    if(mockdata[id].done == false)
    {
        Log.d("iflausekkeessa true", "${mockdata[id].done}")
        val mutableList = mockdata.toMutableList()

        val muutaTaskiDone = Task(
            id = mockdata[id].id,
            title = mockdata[id].title,
            description = mockdata[id].description,
            priority = mockdata[id].priority,
            dueDate = mockdata[id].dueDate,
            done = true   )

        mutableList[id] = muutaTaskiDone
        mockdata = mutableList.toList()
        return mockdata
    }
    else
    {
        Log.d("iflausekkeessa false", "${mockdata[id].done}")
        val mutableList = mockdata.toMutableList()

        val muutaTaskiDone = Task(
            id = mockdata[id].id,
            title = mockdata[id].title,
            description = mockdata[id].description,
            priority = mockdata[id].priority,
            dueDate = mockdata[id].dueDate,
            done = false   )

        mutableList[id] = muutaTaskiDone
        mockdata = mutableList.toList()
        return mockdata
    }

}
fun sortByDate(): List<Task>
{

        val sortedList = mockdata.toMutableList()
        sortedList.sortBy{it.dueDate}

        Log.d("sortattu lista", "$sortedList")
        sortedList.toList()
        return sortedList

}



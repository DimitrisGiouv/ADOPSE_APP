import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.adopse_app.R
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBar = findViewById<EditText>(R.id.SearchBar)

        // Προσθήκη ακροατή συμβάντων στο SearchBar
        searchBar.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Εκτέλεση αναζήτησης όταν ο χρήστης πατήσει το κουμπί αναζήτησης στο πληκτρολόγιο
                val searchTerm = textView.text.toString()
                performSearch(searchTerm)
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun performSearch(searchTerm: String) {
        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        val url = "http://16.171.70.49/module?id=$searchTerm"
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val moduleId = response.getInt("id")

                if (moduleId == searchTerm.toInt()) {
                    val moduleCard1 = layoutInflater.inflate(R.layout.module_long, null) as ConstraintLayout
                    moduleCard1.id = View.generateViewId()

                    val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
                    val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
                    val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
                    val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
                    val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

                    moduleTextView.text = response.getString("name")
                    disModuleTextView.text = response.getString("description")
                    difficultyTextView.text = response.getString("difficultyName")
                    popularityTextView.text = response.getInt("price").toString()
                    ratingTextView.text = response.getInt("rating").toString()

                    parentLayout.addView(moduleCard1)
                } else {
                    // Εδώ μπορείς να εμφανίσεις ένα μήνυμα που λέει ότι δεν βρέθηκαν μαθήματα με το συγκεκριμένο ID
                    Toast.makeText(this, "No modules found with ID $searchTerm", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Εδώ μπορείς να εμφανίσεις ένα μήνυμα λάθους
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

}

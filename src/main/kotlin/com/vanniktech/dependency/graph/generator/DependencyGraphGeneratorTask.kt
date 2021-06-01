package com.vanniktech.dependency.graph.generator

import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension.Generator
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.attribute.Rank.RankDir.RIGHT_TO_LEFT
import guru.nidi.graphviz.attribute.Style
import guru.nidi.graphviz.engine.Graphviz
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
open class DependencyGraphGeneratorTask: DefaultTask() {
  @get:Nested
  lateinit var generator: Generator

  @OutputDirectory
  lateinit var outputDirectory: File

  @get:Internal
  val graph by lazy {
	DependencyGraphGenerator(project, generator).generateGraph()
  }

  @get:Input
  val dotFormatGraph by lazy {
	graph.toString()
  }

  @TaskAction
  fun run() {
	File(outputDirectory, generator.outputFileNameDot).writeText(graph.toString())

	graph.graphAttrs()
		.add(Rank.dir(RIGHT_TO_LEFT)) //    matt was here


	//    .gradient(Color.rgb("888888")) .angle(90)
	graph.graphAttrs()
		.add(Color.BLACK.background())
		.nodeAttrs().add(Color.DARKTURQUOISE.fill())
		.linkAttrs().add(Color.LIGHTBLUE1)
		.nodes().forEach { node ->


		            node.add(
//		  			Color.named(node.name().toString()),
					  Color.BLUE,
		  			Style.lineWidth(4.0), Style.FILLED
//		  Unit

		  )
		}

	var graphviz = Graphviz.fromGraph(
	  graph
	  //    matt was here
	)

	//    matt was here
	graphviz = graphviz.height(1920*4) // .width(1080*4)
	graphviz = graphviz

	generator.outputFormats.forEach {
	  graphviz.render(it).toFile(File(outputDirectory, generator.outputFileName))
	}
  }
}
